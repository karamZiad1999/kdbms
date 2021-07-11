import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Table {

    private Cache cache;
    private RecordFactory recordFactory;
    private FileManager fileManager;
    private RecordIndexManager recordIndexManager;
    private HashMap<String , BlockInformation> recordIndex;
    private HashMap<String, BlockInformation> deletedRecords;



    public Table(String tableName){

        fileManager = new FileManager(tableName);
        recordIndexManager = new RecordIndexManager(tableName);
        recordFactory = new RecordFactory(tableName);
        deletedRecords = new HashMap<String , BlockInformation>();
        recordIndex = new  HashMap<String , BlockInformation>();
        cache = new Cache();

        initializeRecordIndex();

    }

    private void initializeRecordIndex(){

        byte [] byteRecords = recordIndexManager.readAllRecords();
        String records = new String(byteRecords, StandardCharsets.UTF_8);
        String [] stringRecords = records.split("\n");

        for(String record : stringRecords){

            if(record.charAt(0) == '*') addDeletedRecord(record);
            else addRecordIndex(record);
        }

    }

    private void addRecordIndex(String record){
        String [] recordSegments = record.trim().split(" ");
        recordIndex.put(recordSegments[0] , new BlockInformation(recordSegments));
    }


    private void addDeletedRecord(String record){

        String [] recordSegments = record.replaceAll("\\*", " ").trim().split(" ");
        deletedRecords.put(recordSegments[0] , new BlockInformation(recordSegments));

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

        BlockInformation blockInformation = recordIndex.get(primaryKey);
        if(blockInformation == null) return;
        cache.addRecord(new Record(recordFactory.copyHashMapTemplate()), fileManager.getBlock(blockInformation.getByteOffset(), blockInformation.getBlockSize()));

    }

    public void insertRecord (String primaryKey, String recordData){

        byte[] block = recordData.getBytes();
        printToFile(primaryKey, block);
        cache.addRecord(new Record(recordFactory.copyHashMapTemplate()), block);

    }


    private void printToFile(String primaryKey , byte[] block){

        long byteOffset = -1;
        long indexByteOffset = -1;

        if(!deletedRecords.isEmpty()){
            Map.Entry<String , BlockInformation> entryToDelete = null;
            for(Map.Entry<String, BlockInformation> entry : deletedRecords.entrySet()){
                if(block.length <= entry.getValue().getBlockSize()){
                    byteOffset = entry.getValue().getByteOffset();
                    indexByteOffset = entry.getValue().getIndexByteOffset();
                }
                if(entryToDelete != null)  deletedRecords.remove(entryToDelete);
            }
        }

                long index;
                if(byteOffset == -1)
        index = fileManager.printToFile(block);
                else{
                    fileManager.printToFile(block, byteOffset);
                    index = byteOffset;
                }

                BlockInformation blockInformation = new BlockInformation(primaryKey, index , block.length );
                recordIndex.put(primaryKey, blockInformation);

                if(indexByteOffset == -1)
                blockInformation.setIndexByteOffset(recordIndexManager.getFileLength());
                else blockInformation.setIndexByteOffset(recordIndexManager.getFileLength());

                recordIndexManager.printIndexRecord(blockInformation.getIndexRecord());


    }

    public void deleteRecord(String field, String condition, String value){

        for(Map.Entry<String, BlockInformation> entry : recordIndex.entrySet()){

            String primaryKey = entry.getKey();
            cacheRecord(primaryKey);

            if(conditionApplies(cache.getRecord(entry.getKey()), field, condition, value)){

                BlockInformation blockInformation = recordIndex.remove(primaryKey);
                recordIndexManager.deleteRecord(blockInformation.getByteOffset());
                deletedRecords.put(primaryKey, blockInformation);
            }

        }
    }

    public boolean conditionApplies(Record record, String field, String condition, String value){
        return record.checkCondition(field, condition, value);
    }


}
