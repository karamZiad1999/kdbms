package Database.Table;

import Database.Table.Record.LockableRecord;
import Database.Table.Record.RecordInfo;
import Database.Table.Record.Record;
import Database.Table.Record.RecordFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Table {

    private Cache cache;
    private RecordFactory recordFactory;
    private TableManager tableManager;
    private IndexManager indexManager;
    private HashMap<String , RecordInfo> indexMap;
    private ReadWriteLock tableLock;

    public Table(String tableName){
        makeInstances(tableName);
        initializeIndexMap();
    }

    private void makeInstances(String tableName){
        indexMap = new HashMap<String , RecordInfo>();
        indexManager = new IndexManager(tableName, indexMap);
        tableManager = new TableManager(tableName, indexManager);
        recordFactory = new RecordFactory(tableName);
        cache = new Cache();
        tableLock = new ReentrantReadWriteLock();
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
        indexManager.addIndex(recordSegments[0] , new RecordInfo(recordSegments));
    }

    private void addDeletedRecord(String record){
        String [] recordSegments = record.replaceAll("\\*", " ").trim().split(" ");
        indexManager.addDeletedRecord(recordSegments[0] , new RecordInfo(recordSegments));
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
        RecordInfo recordInfo = indexManager.getBlockInfo(primaryKey);
        if(recordInfo == null) return;
        cache.addRecord(new Record(recordFactory.copyHashMapTemplate(), primaryKey), tableManager.getBlock(recordInfo.getByteOffset(), recordInfo.getBlockSize()));
    }

    public void insertRecord (String primaryKey, String recordData){
        byte[] block = recordData.getBytes();
        tableManager.insertRecord(primaryKey, recordData);
        cache.addRecord(new Record(recordFactory.copyHashMapTemplate(), primaryKey), block);
    }

    public void updateRecord(String primaryKey, String recordBlock){
       tableManager.updateRecord(primaryKey, recordBlock);
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

    public void removeRecordIndex(String primaryKey){
        indexMap.remove(primaryKey);
    }

    public void deleteRecord(String primaryKey){
        RecordInfo recordInfo = indexMap.get(primaryKey);
        indexManager.deleteRecord(recordInfo.getIndexByteOffset());
        indexManager.addDeletedRecord(primaryKey, recordInfo);
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

    public LockableRecord getRecordInfo(String primaryKey){
        return indexMap.get(primaryKey);
    }

    public ArrayList<String> getRecordsWithCondition(String field, String condition, String value){
        ArrayList<String> matchingRecords = new ArrayList<String>();
        for(Map.Entry<String, RecordInfo> entry : indexMap.entrySet()){
            String primaryKey = entry.getKey();
            entry.getValue().readLock();
            cacheRecord(primaryKey);
            if(cache.getRecord(primaryKey).checkCondition(field, condition, value)) matchingRecords.add(primaryKey);
            entry.getValue().readUnlock();
        }
        return matchingRecords;
    }
}
