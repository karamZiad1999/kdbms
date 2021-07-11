import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class DB {

    private static DB database;
    private HashMap<String, Table> tables;
    private Set<String> tableSet;

    private  DB(){
        tables = new HashMap<String, Table>();
        tableSet = new HashSet<String>();
        initializeTableSet();
    }

   public static DB getInstance(){
        if(database == null) database = new DB();
        return database;
   }

    public void getRecord(String tableName, String primaryKey){

        Table temp = tables.get(tableName);
        temp.getRecord(primaryKey);

    }

    public void addTable(String tableName, String metaData){

        try(FileWriter  metaDataSrc = new FileWriter( tableName + "-metaData.kdb")){

            metaDataSrc.write(metaData);
            addTable(tableName);

        }catch(IOException e) {
            System.out.println(e);
        }
    }

    public void addTable(String tableName){
        if(tables.get(tableName) != null) return;
        tables.put(tableName, new Table(tableName));
    }

    public void insertRecord(String tableName, String primaryKey, String recordData){
        Table temp;

        if((temp = tables.get(tableName)) == null ){
            if(tableSet.contains(tableName)) addTable(tableName);
            else return;
            temp = tables.get(tableName);
        }

        temp.insertRecord(primaryKey, recordData);
    }

    public void initializeTableSet(){

                try(RandomAccessFile tableSetSrc = new RandomAccessFile("tableSet.kdb", "rw");)
                {
                    String tableName;
                    while((tableName = tableSetSrc.readLine()) != null) tableSet.add(tableName);

                }catch(IOException e){
                    System.out.println(e);
                }
    }

    public void deleteRecord(String tableName, String field, String condition, String value){

        Table table = tables.get(tableName);
        table.deleteRecord(field, condition, value);

    }
}
