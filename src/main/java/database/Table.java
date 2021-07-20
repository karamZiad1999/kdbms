package database;

import SQL.Delete;
import database.Cache;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Table {

    private Cache cache;
    private RecordFactory recordFactory;
    private TableManager tableManager;
    private IndexManager indexManager;
    private HashMap<String , BlockInfo> indexMap;
    private HashMap<String, BlockInfo> deletedRecordsMap;

    public Table(String tableName){
        makeInstances(tableName);
        initializeIndexMap();
    }

    private void makeInstances(String tableName){
        tableManager = new TableManager(tableName);
        indexManager = new IndexManager(tableName);
        recordFactory = new RecordFactory(tableName);
        deletedRecordsMap = new HashMap<String , BlockInfo>();
        indexMap = new  HashMap<String , BlockInfo>();
        cache = new Cache();
    }

    private void initializeIndexMap(){

        byte [] byteRecords = indexManager.readAllRecords();
        if(byteRecords.length <= 0) return;
        String records = new String(byteRecords, StandardCharsets.UTF_8);
        String [] stringRecords = records.split("\n");

        for(String record : stringRecords){

            char firstChar = record.charAt(0);
            if(firstChar == '*') addDeletedRecord(record);
            else if(firstChar == '-') continue;
            else addRecordIndex(record);
        }

    }

    private void addRecordIndex(String record){
        String [] recordSegments = record.trim().split(" ");
        indexMap.put(recordSegments[0] , new BlockInfo(recordSegments));
    }

    private void addDeletedRecord(String record){
        String [] recordSegments = record.replaceAll("\\*", " ").trim().split(" ");
        deletedRecordsMap.put(recordSegments[0] , new BlockInfo(recordSegments));
    }

    public Record getRecord(String primaryKey){

        Record record = cache.getRecord(primaryKey);
        if(record == null){
            cacheRecord(primaryKey);
            record = cache.getRecord(primaryKey);
        }
        return record;

    }

    public void cacheRecord(String primaryKey){

        BlockInfo blockInfo = indexMap.get(primaryKey);
        if(blockInfo == null) return;
        cache.addRecord(new Record(recordFactory.copyHashMapTemplate(), primaryKey), tableManager.getBlock(blockInfo.getByteOffset(), blockInfo.getBlockSize()));

    }

    public void insertRecord (String primaryKey, String recordData){
        byte[] block = recordData.getBytes();
        printToFile(primaryKey, block);
        cache.addRecord(new Record(recordFactory.copyHashMapTemplate(), primaryKey), block);
    }

    private void printToFile(String primaryKey , byte[] block){

        long byteOffset = -1;
        long indexByteOffset = -1;

        if(!deletedRecordsMap.isEmpty()){
            for(Map.Entry<String, BlockInfo> entry : deletedRecordsMap.entrySet()){
                if(block.length <= entry.getValue().getBlockSize()){
                    byteOffset = entry.getValue().getByteOffset();
                    indexByteOffset = entry.getValue().getIndexByteOffset();
                    indexManager.permanentlyDelete(indexByteOffset);
                    deletedRecordsMap.remove(entry.getKey());
                    break;
                }
            }
        }

        if(byteOffset == -1){
            byteOffset = tableManager.printToFile(block);
        }
        else{
            tableManager.printToFile(block, byteOffset);
        }

        BlockInfo blockInfo = new BlockInfo(primaryKey, byteOffset , block.length );
        indexMap.put(primaryKey, blockInfo);

        blockInfo.setIndexByteOffset(indexManager.getFileLength());
        indexManager.printIndexRecord(blockInfo.getIndexRecord());

    }

//    public void selectRecord(String field, String condition, String value){
//        SelectionManager selectionManager = new SelectionManager(field, condition, value);
//        selectionManager.returnIfFound();
//    }

    public void deleteRecord(String field, String condition, String value){
        DeletionManager deletionManager = new DeletionManager(field, condition, value);
        deletionManager.deleteIfFound();
    }

    private class DeletionManager{

        String primaryKeyField;
        String field;
        String condition;
        String value;

        public DeletionManager(String field, String condition, String value){
            this.field = field;
            this.condition = condition;
            this.value = value;
            primaryKeyField = recordFactory.getPrimaryKey();
        }

        public void deleteIfFound(){
            if(isFieldPrimaryKey()) deleteUsingPrimaryKey();
            else deleteUsingCondition();
        }

        private boolean isFieldPrimaryKey(){return field.equalsIgnoreCase(primaryKeyField);}

        private void deleteUsingPrimaryKey(){
            BlockInfo blockInfo = getBlockInfo();
            if(hasInstance(blockInfo)) deleteRecord(value, blockInfo);
        }

        private BlockInfo getBlockInfo(){return indexMap.remove(value);}

        private void deleteRecord(String value, BlockInfo blockInfo){
            indexManager.deleteRecord(blockInfo.getIndexByteOffset());
            deletedRecordsMap.put(value, blockInfo);
        }

        private void deleteUsingCondition(){
            Iterator recordIterator = indexMap.entrySet().iterator();

            while (recordIterator.hasNext()) {
                Map.Entry entry = (Map.Entry)recordIterator.next();
                String primaryKey = (String) entry.getKey();
                cacheRecord(primaryKey);
                Record record = cache.getRecord(primaryKey);

                if(hasInstance(record)) deleteIfMatch(record, recordIterator, primaryKey);
            }
        }

        private void deleteIfMatch(Record record, Iterator recordIterator, String primaryKey){
            if(conditionApplies(record , field, condition, value)){
                System.out.println("deleted");
                BlockInfo blockInfo = indexMap.get(primaryKey);
                recordIterator.remove();
                indexManager.deleteRecord(blockInfo.getIndexByteOffset());
                deletedRecordsMap.put(primaryKey, blockInfo);
            }
        }

        private boolean hasInstance(Object obj){return (obj != null);}
    }


    public boolean conditionApplies(Record record, String field, String condition, String value){
        return record.checkCondition(field, condition, value);
    }

    public void updateRecord(String field, String condition, String value, HashMap<String,String> updates){
        UpdateManager updateManager = new UpdateManager(field, condition, value, updates);
        updateManager.updateIfFound();
    }


    private class UpdateManager{


        String primaryKeyField;
        String field;
        String condition;
        String value;
        HashMap<String,String> updates;

            public UpdateManager(String field, String condition, String value, HashMap<String,String> updates){
                this.field = field;
                this.condition = condition;
                this.value = value;
                primaryKeyField = recordFactory.getPrimaryKey();
                this.updates = updates;
            }

            public void updateIfFound(){
                if(isFieldPrimaryKey()) updateUsingPrimaryKey();
                else updateUsingCondition();
            }

            private boolean isFieldPrimaryKey(){return field.equalsIgnoreCase(primaryKeyField);}

            private void updateUsingPrimaryKey(){
                cacheRecord(value);
                Record record = cache.getRecord(value);
                record.updateRecord(updates);
                tableManager.printToFile(record.getRecordBlock().getBytes());
            }

            private BlockInfo getBlockInfo(){return indexMap.remove(value);}

            private void updateUsingCondition(){
                Iterator recordIterator = indexMap.entrySet().iterator();

                while (recordIterator.hasNext()) {
                    Map.Entry entry = (Map.Entry)recordIterator.next();
                    String primaryKey = (String) entry.getKey();
                    cacheRecord(primaryKey);
                    Record record = cache.getRecord(primaryKey);

                    if(hasInstance(record)) updateIfMatch(record, recordIterator, primaryKey);
                }
            }

            private void updateIfMatch(Record record, Iterator recordIterator, String primaryKey){
                if(conditionApplies(record , field, condition, value)){
                    record.updateRecord(updates);
                }
            }

            private boolean hasInstance(Object obj){return (obj != null);}
        }


}
