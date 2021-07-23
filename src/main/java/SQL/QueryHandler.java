package SQL;
import database.KDBMS;

import java.io.PrintWriter;

public class QueryHandler {

    KDBMS database;


    public QueryHandler(){
        database = KDBMS.getInstance();
    }

    public void handleAction(Create create){
        if(database.isTableNameLegal(create.getTableName())){
            database.createTable(create.getTableName(), create.getMetaDataInString());
            System.out.println("table created");
        }


    }

    public void handleAction(Insert insert){

        database.insertRecord(insert.getTableName(), insert.getPrimaryKey(), insert.getRecordBlock());
    }

    public void handleAction(Delete deleteAction){
        database.deleteRecord(deleteAction.getTableName(), deleteAction.getField(), deleteAction.getCondition() ,  deleteAction.getValue());
    }

    public void handleAction(Update updateAction){
        database.updateRecord(updateAction.getTableName(),updateAction.getField(),updateAction.getCondition() , updateAction.getValue(),updateAction.getUpdates());
    }

    public void handleAction(Select selectAction, PrintWriter out){
        if(selectAction.isCondition()) database.selectRecord(selectAction.getTableName(), selectAction.getField(), selectAction.getCondition(), selectAction.getValue(), out);
        else database.selectAllRecords(selectAction.getTableName(), out);

    }
}
