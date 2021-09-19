package com.atypon.database.table;

import com.atypon.database.table.Record.LockableIndex;
import com.atypon.database.table.Record.Record;
import com.atypon.database.table.Record.RecordFactory;
import com.atypon.database.table.Record.RecordInfo;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface InsertableTable {
    public ReentrantReadWriteLock getLock();
    public void insertRecord(Record record);
    public RecordFactory getRecordFactory();
}
