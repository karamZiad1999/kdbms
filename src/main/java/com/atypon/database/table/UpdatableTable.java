package com.atypon.database.table;

import com.atypon.database.table.Record.LockableIndex;
import com.atypon.database.table.Record.Record;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface UpdatableTable {
    public Record getRecord(String primaryKey);
    public boolean isFieldPrimaryKey(String primaryKey);
    public void updateRecord(String primaryKey,String record);
    public RecordIterator getRecordIterator();
    public LockableIndex getLockableIndex(String primaryKey);
    public ReentrantReadWriteLock getLock();
}
