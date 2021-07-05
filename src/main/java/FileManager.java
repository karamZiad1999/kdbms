import java.io.RandomAccessFile;

public class FileManager {

    RandomAccessFile tableSrc;

    public FileManager(String tableSrc){
        try {
            this.tableSrc = new RandomAccessFile(tableSrc, "rw");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public byte[] getBlock(int byteOffset, int blockSize){
        byte[] block = new byte[blockSize];
       try{

            tableSrc.seek(byteOffset);
            tableSrc.read(block);

       }catch(Exception e){
           System.out.println(e);
       }

       return block;
    }


}
