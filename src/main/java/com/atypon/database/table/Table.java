package com.atypon.database.table;

import com.atypon.database.table.Record.LockableIndex;
import com.atypon.database.table.Record.RecordInfo;
import com.atypon.database.table.Record.Record;
import com.atypon.database.table.Record.RecordFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class Table implements InsertableTable, SelectableTable, UpdatableTable, DeletableTable {

    private Cache cache;
    private RecordFactory recordFactory;
    private TableManager tableManager;
    private ReentrantReadWriteLock lock;

    public Table(String tableName){
        tableManager = new TableManager(tableName);
        recordFactory = new RecordFactory(tableName);
        cache = new Cache();
        lock = new ReentrantReadWriteLock();
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
        RecordInfo recordInfo = tableManager.getRecordInfo(primaryKey);
        if(recordInfo == null) return;
        cache.addRecord(new Record(recordFactory.copyHashMapTemplate(), primaryKey), tableManager.getBlock(primaryKey));
    }

    public void insertRecord (String primaryKey, String recordData){
        tableManager.insertRecord(primaryKey, recordData);
        cache.addRecord(new Record(recordFactory.copyHashMapTemplate(), primaryKey), recordData);
    }

    public void updateRecord(String primaryKey, String recordBlock){
       tableManager.updateRecord(primaryKey, recordBlock);
    }

    public boolean isFieldPrimaryKey(String fieldName){
        return fieldName.equalsIgnoreCase(recordFactory.getPrimaryKey());
    }

    public void deleteRecord(String primaryKey){
        tableManager.deleteRecord(primaryKey);
    }

    private class TableRecordIterator implements RecordIterator {
        private Iterator iterator;

        public TableRecordIterator(){
            iterator = tableManager.getIndexIterator();
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

    public LockableIndex getRecordInfo(String primaryKey){
        return tableManager.getRecordInfo(primaryKey);
    }

    public ReentrantReadWriteLock getLock(){
        return lock;
    }

}
