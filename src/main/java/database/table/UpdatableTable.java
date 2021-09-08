package database.table;

import database.table.Record.LockableIndex;
import database.table.Record.Record;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface UpdatableTable {
    public Record getRecord(String primaryKey);
    public boolean isFieldPrimaryKey(String primaryKey);
    public void updateRecord(String primaryKey,String record);
    public RecordIterator getRecordIterator();
    public LockableIndex getRecordInfo(String primaryKey);
    public ReentrantReadWriteLock getLock();
}
