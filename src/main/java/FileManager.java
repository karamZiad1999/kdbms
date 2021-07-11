import java.io.IOException;
import java.io.RandomAccessFile;

public class FileManager {

    RandomAccessFile tableSrc;


    public FileManager(String tableName){
        try {
            this.tableSrc = new RandomAccessFile(tableName + ".kdb", "rw");
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
            printToFile(b, tableSrc.length());
            return tableSrc.length();
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

}
