package database.table;

import database.table.Record.LockableIndex;
import database.table.Record.Record;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface DeletableTable{
    public boolean isFieldPrimaryKey(String field);
    public void deleteRecord(String primaryKey);
    public Record getRecord(String primaryKey);
    public LockableIndex getRecordInfo(String primaryKey);
    public RecordIterator getRecordIterator();
    public ReentrantReadWriteLock getLock();
}
