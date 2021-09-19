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
    private String primaryKey;

    public Table(String schemaName, String tableName){
        tableManager = new TableManager(schemaName, tableName);
        recordFactory = new RecordFactory(schemaName, tableName);
        primaryKey = recordFactory.getPrimaryKey();
        cache = new Cache(tableManager, recordFactory);
        lock = new ReentrantReadWriteLock();
    }

    public Record getRecord(String primaryKey){
        Record record = cache.getRecord(primaryKey);
        return record;
    }

    public void insertRecord (Record record){
        String primaryKey = record.getPrimaryKey();
        if(tableManager.getRecordInfo(primaryKey)!=null) return;
        tableManager.insertRecord(primaryKey, record);
        cache.addRecord(record);
    }

    @Override
    public RecordFactory getRecordFactory() {
        return recordFactory;
    }

    public void updateRecord(String primaryKey, String recordBlock){
       tableManager.updateRecord(primaryKey, recordBlock);
    }

    public boolean isFieldPrimaryKey(String fieldName){
        return fieldName.equalsIgnoreCase(primaryKey);
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

    public LockableIndex getLockableIndex(String primaryKey){
        return tableManager.getRecordInfo(primaryKey);
    }

    public ReentrantReadWriteLock getLock(){
        return lock;
    }

    public String getPrimaryKey(){
        return primaryKey;
    }

    public String getMeta(){
        return recordFactory.getMeta();
    }

}
