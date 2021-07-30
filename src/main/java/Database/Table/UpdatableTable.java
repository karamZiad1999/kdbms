package Database.Table;

import Database.Table.Record.LockableIndex;
import Database.Table.Record.Record;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface UpdatableTable {
    public Record getRecord(String primaryKey);
    public boolean isFieldPrimaryKey(String primaryKey);
    public void updateRecord(String primaryKey,String record);
    public RecordIterator getRecordIterator();
    public LockableIndex getRecordInfo(String primaryKey);
    public ReentrantReadWriteLock getLock();
}
