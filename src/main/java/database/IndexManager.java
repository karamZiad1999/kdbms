package database;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class IndexManager {

    RandomAccessFile indexRecordSrc;


    public IndexManager(String tableName){

        try {
            this.indexRecordSrc = new RandomAccessFile(tableName + "-indexSrc.kdb", "rw");
        }catch(Exception e){
            System.out.println(e);
        }

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


}
