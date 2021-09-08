package com.atypon.database.table;

import com.atypon.database.table.Record.LockableIndex;
import com.atypon.database.table.Record.Record;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface DeletableTable{
    public boolean isFieldPrimaryKey(String field);
    public void deleteRecord(String primaryKey);
    public Record getRecord(String primaryKey);
    public LockableIndex getRecordInfo(String primaryKey);
    public RecordIterator getRecordIterator();
    public ReentrantReadWriteLock getLock();
}
