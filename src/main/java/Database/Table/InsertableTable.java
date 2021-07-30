package Database.Table;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface InsertableTable {
    public ReentrantReadWriteLock getLock();
    public void insertRecord(String primaryKey, String record);
}
