package Database.Table.Record;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface LockableIndex {
    public void readLock();
    public void readUnlock();
    public void writeLock();
    public void writeUnlock();
    public ReentrantReadWriteLock getLock();
    public String getPrimaryKey();
}
