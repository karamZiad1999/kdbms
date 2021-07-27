package Database.Table;

import Database.Table.Record.RecordInfo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class IndexManager {

    private RandomAccessFile indexRecordSrc;
    private HashMap<String, RecordInfo> deletedRecordsMap;
    private HashMap<String , RecordInfo> indexMap;

    public IndexManager(String tableName, HashMap<String , RecordInfo> indexMap ){
        try {
            this.indexRecordSrc = new RandomAccessFile(tableName + "-index.kdb", "rw");
        }catch(Exception e){
            System.out.println(e);
        }
        this.indexMap = indexMap;
        deletedRecordsMap = new HashMap<String , RecordInfo>();
    }

    public RecordInfo getBlockInfo(String primaryKey){
        return indexMap.get(primaryKey);
    }


    public String getRecord(long byteOffset){
        String record = " ";
        try{
            indexRecordSrc.seek(byteOffset);
            record = indexRecordSrc.readLine();
        }catch(Exception e){
            System.out.println(e);
        }
        return record;
    }

    public long getFileLength(){
        try{
            return indexRecordSrc.length();
        }catch(IOException e){
            System.out.println(e);
        }
        return 0;
    }

    public void printIndexRecord(String indexRecord){
        try{
            printIndexRecord(indexRecord, indexRecordSrc.length());
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void printIndexRecord(String indexRecord, long byteOffset){
        byte[] indexRecordBytes = indexRecord.getBytes();
        try{
            indexRecordSrc.seek(byteOffset);
            indexRecordSrc.write(indexRecordBytes);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void deleteRecord(long byteOffset){
        printIndexRecord("*", byteOffset);
    }

    public void permanentlyDelete(long byteOffset){ printIndexRecord("-", byteOffset);}

    public void addDeletedRecord(String primaryKey, RecordInfo recordInfo){
        deletedRecordsMap.put(primaryKey, recordInfo);
    }

    public byte[] readAllRecords(){
        byte[] b = null;

        try{
            indexRecordSrc.seek(0);
            b = new byte [(int)indexRecordSrc.length()];
            indexRecordSrc.read(b);
        }catch(IOException e){
            System.out.println(e);
        }
        return b;
    }


    public long getOverwriteRecord(int blockSize){
        long byteOffset = -1;
        if(!deletedRecordsMap.isEmpty()){
            for(Map.Entry<String, RecordInfo> entry : deletedRecordsMap.entrySet()){
                if(blockSize <= entry.getValue().getBlockSize()){
                    byteOffset = entry.getValue().getByteOffset();
                    long indexByteOffset = entry.getValue().getIndexByteOffset();
                    permanentlyDelete(indexByteOffset);
                    deletedRecordsMap.remove(entry.getKey());

                    break;
                }
            }
        }
        return byteOffset;
    }

    public void addIndex(String primaryKey, RecordInfo recordInfo){
        indexMap.put(primaryKey, recordInfo);
    }
}
