import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


 public class DB {
    private static DB database;
    private HashMap<String, Table> tables;

    private  DB(){
        tables = new HashMap<String, Table>();
    }

   public static DB getInstance(){
        if(database == null) database = new DB();
        return database;
   }

    public void getRecord(String tableName, int primaryKey){

        Table temp = tables.get(tableName);
        temp.getRecord(primaryKey);

    }

    public void addTable(String tableName, String tableSrc, String metaDataSrc, String recordIndex){

    }
//
//    public void isValidTableName(){
//
//    }

}
