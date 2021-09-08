package database.table;

import database.table.Record.RecordInfo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IndexManager {

    private RandomAccessFile indexRecordSrc;
    private HashMap<String, RecordInfo> deletedRecordsMap;
    private HashMap<String , RecordInfo> indexMap;

    public IndexManager(String tableName){
        try {
            this.indexRecordSrc = new RandomAccessFile(tableName + "-index.kdb", "rw");
        }catch(Exception e){
            System.out.println(e);
        }
        indexMap = new HashMap<String, RecordInfo>();
        deletedRecordsMap = new HashMap<String , RecordInfo>();
        initializeIndexMap();
    }

    public RecordInfo getRecordInfo(String primaryKey){
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

    public Iterator getIndexIterator(){
        return indexMap.entrySet().iterator();
    }

    public RecordInfo removeIndex(String primaryKey){
        return indexMap.remove(primaryKey);
    }

    private void initializeIndexMap(){
        byte [] byteRecords = readAllRecords();
        if(byteRecords.length <= 0) return;
        String records = new String(byteRecords, StandardCharsets.UTF_8);
        String [] stringRecords = records.split("\n");

        for(String record : stringRecords){
            char firstChar = record.charAt(0);
            if(firstChar == '*') addDeletedRecord(record);
            else if(firstChar == '-') continue;
            else addRecordIndex(record);
        }
    }

    private void addRecordIndex(String record){
        String [] recordSegments = record.trim().split(" ");
        addIndex(recordSegments[0] , new RecordInfo(recordSegments));
    }

    private void addDeletedRecord(String record){
        String [] recordSegments = record.replaceAll("\\*", " ").trim().split(" ");
        addDeletedRecord(recordSegments[0] , new RecordInfo(recordSegments));
    }
}
