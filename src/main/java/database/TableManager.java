package database;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class TableManager {

    RandomAccessFile tableSrc;
    final int REQUEST_FAILED = -1;
    IndexManager indexManager;

    public TableManager(String tableName, IndexManager indexManager){
        try {
            this.tableSrc = new RandomAccessFile(getFilePath(tableName), "rw");
        }catch(Exception e){
            System.out.println(e);
        }
        this.indexManager = indexManager;
    }

    public void insertRecord(String primaryKey, String record){
        long byteOffset = indexManager.getOverwriteRecord(record.length());
        byte [] b = record.getBytes();
        if(byteOffset == -1) byteOffset = printToFile(b);
        else printToFile(b, byteOffset);
        BlockInfo blockInfo = new BlockInfo(primaryKey, byteOffset, record.length());
        blockInfo.setIndexByteOffset(indexManager.getFileLength());
        indexManager.printIndexRecord(blockInfo.getIndexRecord());
        indexManager.addIndex(primaryKey, blockInfo);
    }

    public void updateRecord(String primaryKey, String recordBlock){
        byte [] b = recordBlock.getBytes();
        BlockInfo blockInfo = indexManager.getBlockInfo(primaryKey);
        long byteOffset = blockInfo.getByteOffset();
        printToFile(b, byteOffset);
        blockInfo.setBlockSize(b.length);
        indexManager.printIndexRecord(blockInfo.getIndexRecord(), blockInfo.getIndexByteOffset());
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
            printToFile(b, length);
            return length;
        }catch(IOException e){
            System.out.println(e);
        }
        return 0;
    }

    public void printToFile(byte[] b, long byteOffset)  {
        try{
            tableSrc.seek(byteOffset);
            tableSrc.write(b);
        }catch (IOException e){
            System.out.println(e);
        }



    }

    private long getFileLength()  throws IOException {
        return tableSrc.length();
    }

    private String getFilePath(String tableName){
        return tableName + ".kdb";
    }

}
