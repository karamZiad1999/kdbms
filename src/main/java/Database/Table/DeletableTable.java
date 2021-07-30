package Database.Table;

import Database.Table.Record.LockableIndex;
import Database.Table.Record.Record;
import Database.Table.Record.RecordInfo;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface DeletableTable{
    public boolean isFieldPrimaryKey(String field);
    public void deleteRecord(String primaryKey);
    public Record getRecord(String primaryKey);
    public LockableIndex getRecordInfo(String primaryKey);
    public RecordIterator getRecordIterator();
    public ReentrantReadWriteLock getLock();
}
