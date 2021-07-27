package Database.StatementManager;

import Database.Table.Record.Record;
import Database.Table.RecordIterator;
import Database.Table.Table;
import SQL.Statement.DeleteStatement;

import java.io.PrintWriter;

public class DeleteManager implements StatementManager {
    Table table;
    String field;
    String condition;
    String value;
    PrintWriter out;

    public DeleteManager(DeleteStatement delete, Table table) {
        this.table = table;
        field = delete.getField();
        condition = delete.getCondition();
        value = delete.getValue();
        out = delete.getOutputStream();
    }

    public void execute(){
        if(table.isFieldPrimaryKey(field)) deleteUsingPrimaryKey();
        else deleteUsingCondition();
        if(out != null ) out.println("Transaction Successful\n");
    }

    private void deleteUsingPrimaryKey(){
        table.deleteRecord(value);
        table.removeRecordIndex(value);
    }

    private void deleteUsingCondition(){
        RecordIterator recordIterator = table.getRecordIterator();
        Record record;
        while(recordIterator.hasNext()){
            record = recordIterator.getNextRecord();
            if(record.checkCondition(field, condition, value)){
                table.deleteRecord(record.getPrimaryKey());
                recordIterator.deleteRecord();
            }
        }
    }
}

