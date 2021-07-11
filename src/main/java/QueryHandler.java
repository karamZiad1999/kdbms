import java.io.*;

public class QueryHandler {

    DB database;
    FileWriter metaDataSrc;
    File tableSrc;
    File indexSrc;

    public QueryHandler(){
        database = DB.getInstance();

    }

    public void handleAction(Create create){
        try{

            String tableName = create.getTableName();
            tableSrc = new File(tableName+".kdb");
            metaDataSrc = new FileWriter( tableName + "-metaData.kdb");
            indexSrc = new File(tableName + "-indexSrc.kdb");

            tableSrc.createNewFile();
            indexSrc.createNewFile();

            metaDataSrc.write(create.getMetaDataInString());

       }catch(IOException e){
            System.out.println(e);
        }finally {
            try{
                metaDataSrc.close();
            }catch (IOException e){
                System.out.println(e);
            }

        }
    }

    public void handleAction(Insert insert){

        database.insertRecord(insert.getTableName(), insert.getPrimaryKey(), insert.getRecordBlock());
    }

    public void handleAction(Delete deleteAction){
        database.deleteRecord(deleteAction.getTableName(), deleteAction.getField(), deleteAction.getCondition() ,  deleteAction.getValue());
    }
}
