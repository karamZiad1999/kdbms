package Database.Table.Record;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface LockableRecord{
    public void readLock();
    public void readUnlock();
    public void writeLock();
    public void writeUnlock();
    public ReentrantReadWriteLock getLock();
}
