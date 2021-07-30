package Database.Table;


import Database.Table.Record.LockableIndex;
import Database.Table.Record.Record;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface SelectableTable{
    public boolean isFieldPrimaryKey(String field);
    public RecordIterator getRecordIterator();
    public LockableIndex getRecordInfo(String primaryKey);
    public Record getRecord(String primaryKey);
    public ReentrantReadWriteLock getLock();
}
