package database;

import SQL.Delete;
import database.Cache;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

public class Table {

    private Cache cache;
    private RecordFactory recordFactory;
    private TableManager tableManager;
    private IndexManager indexManager;
    private HashMap<String , BlockInfo> indexMap;
    private HashMap<String, BlockInfo> deletedRecordsMap;
    private ReadWriteLock tableLock;

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



        public void lockTableRead(){
            tableLock.readLock().lock();
        }

        public void lockTableWrite(){
            tableLock.writeLock().lock();
        }

        public void unlockTableWrite(){
            tableLock.writeLock().unlock();
        }

        public boolean isFieldPrimaryKey(String fieldName){
            return fieldName.equalsIgnoreCase(recordFactory.getPrimaryKey());
        }

        public BlockInfo getBlockInfo(String primaryKey){
            return indexMap.remove(primaryKey);
        }

        public void removeRecordIndex(String primaryKey){
            indexMap.remove(primaryKey);
        }
        public void deleteRecord(String primaryKey){
            BlockInfo blockInfo = indexMap.get(primaryKey);
            indexManager.deleteRecord(blockInfo.getIndexByteOffset());
            deletedRecordsMap.put(primaryKey, blockInfo);
        }

        private class TableRecordIterator implements RecordIterator {
            private Iterator iterator;

            public TableRecordIterator(){
                iterator = indexMap.entrySet().iterator();
            }

            public Record getNextRecord(){
                Map.Entry entry = (Map.Entry)iterator.next();
                String primaryKey = (String) entry.getKey();
                cacheRecord(primaryKey);
                return cache.getRecord(primaryKey);
            }

            public boolean hasNext(){
                return iterator.hasNext();
            }

            public void deleteRecord(){
                iterator.remove();
            }
        }

        public TableRecordIterator getRecordIterator(){
            return new TableRecordIterator();
        }






}
