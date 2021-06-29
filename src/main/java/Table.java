import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Table {

    RandomAccessFile tableSrc;
    RandomAccessFile  metaDataSrc;
    TableMetaData metaData;
    HashMap<Integer, Record> records;

    public Table(String tableSrc, String metaDataSrc){

        try {
            this.tableSrc = new RandomAccessFile(tableSrc, "rw");
            this.metaDataSrc = new RandomAccessFile(metaDataSrc, "rw");
        } catch(Exception e){
            System.out.println(e);
        }

        RecordFactory recordFactory = new RecordFactory(this.metaDataSrc);
    }




}
