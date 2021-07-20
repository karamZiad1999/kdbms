package database;

import java.io.IOException;
import java.io.RandomAccessFile;

public class TableManager {

    RandomAccessFile tableSrc;
    final int REQUEST_FAILED = -1;

    public TableManager(String tableName){
        try {
            this.tableSrc = new RandomAccessFile(getFilePath(tableName), "rw");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public byte[] getBlock(long byteOffset, int blockSize){
        byte[] block = new byte[blockSize];
       try{
            tableSrc.seek(byteOffset);
            tableSrc.read(block);

       }catch(Exception e){
           System.out.println(e);
       }

       return block;
    }

    public long printToFile(byte[] b)  {
        try {
            long length = getFileLength();
            printToFile(b, tableSrc.length());
            return length;
        }catch(IOException e){
            System.out.println(e);
        }
        return REQUEST_FAILED;
    }

    public void printToFile(byte[] b, long byteOffset)  {
        try{
            tableSrc.seek(byteOffset);
            tableSrc.write(b);
        }catch (IOException e){
            System.out.println(e);
        }
    }

    private long getFileLength()  throws IOException{
        return tableSrc.length();
    }

    private String getFilePath(String tableName){
        return tableName + ".kdb";
    }

}
