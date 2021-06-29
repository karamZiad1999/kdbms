import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


public class DB {
    HashMap<String, Table> tables;

    public DB(){
        tables = new HashMap<String, Table>();
    }
    public void createTable(String fileName, String tableSrc, String metaDataSrc){
        tables.put(fileName, new Table(tableSrc, metaDataSrc));

    }


}