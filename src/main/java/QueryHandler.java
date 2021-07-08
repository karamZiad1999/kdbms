import java.io.*;

public class QueryHandler {

    DB database;
    FileWriter metaDataSrc;
    File tableSrc;
    File indexSrc;

    public QueryHandler(){
        database = DB.getInstance();

    }

    public void handleAction(Create createObject){
        try{

            String tableName = createObject.getTableName();
            tableSrc = new File(tableName+".kdb");
            metaDataSrc = new FileWriter( tableName + "-metaData.kdb");
            indexSrc = new File(tableName + "-indexSrc.kdb");

            tableSrc.createNewFile();
            indexSrc.createNewFile();

            metaDataSrc.write(createObject.getMetaDataInString());
            metaDataSrc.close();
       }catch(IOException e){
            System.out.println(e);
        }
    }
}
