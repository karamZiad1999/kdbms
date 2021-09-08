package database.table;

import database.table.Record.RecordInfo;
import java.util.Iterator;

public class TableManager {

    IndexManager indexManager;
    TableSrcManager tableSrcManager;

    public TableManager(String tableName){
        tableSrcManager = new TableSrcManager(tableName);
        indexManager = new IndexManager(tableName);
    }

    public void insertRecord(String primaryKey, String record){
        long byteOffset = indexManager.getOverwriteRecord(record.length());
        byte [] b = record.getBytes();
        if(byteOffset == -1) byteOffset = tableSrcManager.printToFile(b);
        else tableSrcManager.printToFile(b, byteOffset);
        RecordInfo recordInfo = new RecordInfo(primaryKey, byteOffset, record.length());
        recordInfo.setIndexByteOffset(indexManager.getFileLength());
        indexManager.printIndexRecord(recordInfo.getIndexRecord());
        indexManager.addIndex(primaryKey, recordInfo);
    }

    public void updateRecord(String primaryKey, String recordBlock){
        byte [] b = recordBlock.getBytes();
        RecordInfo recordInfo = indexManager.getRecordInfo(primaryKey);
        long byteOffset = recordInfo.getByteOffset();
        tableSrcManager.printToFile(b, byteOffset);
        recordInfo.setBlockSize(b.length);
        indexManager.printIndexRecord(recordInfo.getIndexRecord(), recordInfo.getIndexByteOffset());
    }

    public String getBlock(String primaryKey)
    {
        RecordInfo recordInfo = indexManager.getRecordInfo(primaryKey);
        return tableSrcManager.getBlock(recordInfo.getByteOffset(), recordInfo.getBlockSize());
    }

    public RecordInfo getRecordInfo(String primaryKey){
        return indexManager.getRecordInfo(primaryKey);
    }

    public void removeIndex(String primaryKey){
        indexManager.removeIndex(primaryKey);
    }

    public void deleteRecord(String primaryKey){
        RecordInfo recordInfo = indexManager.removeIndex(primaryKey);
        if(recordInfo != null) {
            indexManager.deleteRecord(recordInfo.getIndexByteOffset());
            indexManager.addDeletedRecord(primaryKey, recordInfo);
        }
    }

    public Iterator getIndexIterator(){
        return indexManager.getIndexIterator();
    }

}
