package Database.StatementManager;

import Database.KDBMS;
import Database.Table.Table;
import SQL.Statement.CreateStatement;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CreateManager implements StatementManager {

    String tableName;
    String primaryKey;
    String metaData;

    public CreateManager(CreateStatement create, Table table) {
        tableName = create.getTableName();
        primaryKey = create.getPrimaryKey();
        metaData = create.getMetaDataInString();
    }

    public void execute(){
        KDBMS database = KDBMS.getInstance();
        database.addTable(tableName);

        try{
            createTableSrc();
            createIndexSrc();
            createMetaDataSrc();
            printMetaData();
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void createTableSrc() throws IOException{
        File tableSrc = new File(tableName + ".kdb");
        tableSrc.createNewFile();
    }

    public void createIndexSrc() throws IOException{
        File indexSrc  = new File(tableName + "-index.kdb");
        indexSrc.createNewFile();
    }

    public void createMetaDataSrc() throws IOException{
        File metaDataSrc = new File(tableName + "-metaData.kdb");
        metaDataSrc.createNewFile();
    }

    public void printMetaData(){
        try(FileWriter fileWriter = new FileWriter(tableName + "-metaData.kdb", true)){
            fileWriter.write(metaData);
        }catch(IOException e){
            System.out.println(e);
        }
    }



}
