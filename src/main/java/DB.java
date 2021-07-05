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

    public void createTable(String tableName, String tableSrc, String metaDataSrc){
        tables.put(tableName, new Table(tableSrc, metaDataSrc));
    }

    public void getRecord(String tableName, int primaryKey){
        Table temp = tables.get(tableName);
        temp.getRecord(primaryKey);

    }
}
