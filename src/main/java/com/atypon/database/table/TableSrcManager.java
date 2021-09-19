package com.atypon.database.table;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class TableSrcManager {
    File tableSrc;

    public TableSrcManager(String schemaName, String tableName){
        tableSrc = new File(schemaName + "/" +tableName+ ".kdb");
    }

    public String getBlock(long byteOffset, int blockSize){
        byte[] block = new byte[blockSize];
        try(RandomAccessFile tableSrcReader = new RandomAccessFile(tableSrc, "rw")){
            tableSrcReader.seek(byteOffset);
            tableSrcReader.read(block);
        }catch(Exception e){
            System.out.println(e);
        }
        String stringBlock = new String(block, StandardCharsets.UTF_8);
        return stringBlock;
    }

    public long printToFile(byte[] b)  {
       try{
            long length = getFileLength();
            printToFile(b, length);
            return length;
        }catch(IOException e){
            System.out.println(e);
        }
        return 0;
    }

    public void printToFile(byte[] b, long byteOffset){
        try(RandomAccessFile tableSrcWriter = new RandomAccessFile(tableSrc, "rw")){
            tableSrcWriter.seek(byteOffset);
            tableSrcWriter.write(b);
        }catch (IOException e){
            System.out.println(e);
        }
    }

    private long getFileLength()  throws IOException{
        return tableSrc.length();
    }

}
