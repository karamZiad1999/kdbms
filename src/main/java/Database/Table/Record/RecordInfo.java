package Database.Table.Record;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RecordInfo implements LockableIndex {
    private long blockByteOffset;
    private int blockSize;
    private long indexByteOffset;
    private String primaryKey;
    private ReentrantReadWriteLock lock;

    public RecordInfo(String [] record){
        lock = new ReentrantReadWriteLock();
        parseIndexRecord(record);
    }
    public RecordInfo(String primaryKey, long blockByteOffset, int blockSize){
        this.blockByteOffset = blockByteOffset;
        this.blockSize = blockSize;
        this.primaryKey = primaryKey;
        lock = new ReentrantReadWriteLock();
    }

    public int getBlockSize() {
        return blockSize;
    }

    public long getByteOffset(){
        return blockByteOffset;
    }

    public long getIndexByteOffset(){
        return indexByteOffset;
    }



    public void parseIndexRecord(String[] indexRecord){
        primaryKey = indexRecord[0];
        blockByteOffset =  Long.parseLong(indexRecord[1]);
        blockSize =  Integer.parseInt(indexRecord[2]);
        indexByteOffset =  Long.parseLong(indexRecord[3]);
    }

    public void setIndexByteOffset(long indexByteOffset){
        this.indexByteOffset = indexByteOffset;
    }

    public void setBlockSize(int blockSize){

        this.blockSize = blockSize;
    }


    public String getIndexRecord(){
        StringBuilder indexRecord = new StringBuilder();

        indexRecord.append(" ");
        indexRecord.append(primaryKey);
        indexRecord.append(" ");
        indexRecord.append(blockByteOffset);
        indexRecord.append(" ");
        indexRecord.append(blockSize);
        indexRecord.append(" ");
        indexRecord.append(indexByteOffset);
        indexRecord.append("\n");

        return indexRecord.toString();
    }

    public void readLock(){
        lock.readLock().lock();
    }

    public void readUnlock(){lock.readLock().unlock();}

    public void writeLock(){lock.writeLock().lock();}

    public void writeUnlock(){lock.writeLock().unlock();}

    public ReentrantReadWriteLock getLock(){
        return lock;
    }

    public String getPrimaryKey(){
        return primaryKey;
    }
}