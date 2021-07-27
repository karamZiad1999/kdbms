package Database.StatementManager;

import Database.Table.Record.LockableRecord;
import Database.Table.Record.Record;
import Database.Table.RecordIterator;
import Database.Table.Table;
import SQL.Statement.SelectStatement;

import java.io.PrintWriter;
import java.util.ArrayList;

public class SelectManager implements StatementManager {
    private Table table;
    private String field;
    private String condition;
    private String value;
    private PrintWriter out;
    private boolean isCondition;

    public SelectManager(SelectStatement select, Table table) {
        this.table = table;
        this.field = select.getField();
        this.condition = select.getCondition();
        this.value = select.getValue();
        this.out = select.getOutputStream();
        this.isCondition = select.isCondition();
    }
    public void execute(){
        if(isCondition) select();
        else selectAll();
    }

    public void select(){
        if(table.isFieldPrimaryKey(field)) selectUsingPrimaryKey();
        else selectUsingCondition();
    }

    public void selectAll(){

        RecordIterator recordIterator = table.getRecordIterator();
        Record record;
        StringBuilder records = new StringBuilder();
        while(recordIterator.hasNext()){
            record = recordIterator.getNextRecord();
            records.append(record.printRecord());
        }
        if(out != null) out.println(records.toString());
    }

    private void selectUsingPrimaryKey(){
        selectUsingPrimaryKey(value);
    }

    private void selectUsingPrimaryKey(String primaryKey){
        LockableRecord recordLock= table.getRecordInfo(primaryKey);
        recordLock.readLock();
        Record record = table.getRecord(primaryKey);
        recordLock.readUnlock();
        out.println(record.printRecord());
    }

    private void selectUsingCondition(){
        ArrayList<String> matchingRecords = table.getRecordsWithCondition(field, condition, value);
        for(String primaryKey : matchingRecords) {
            selectUsingPrimaryKey(primaryKey);
        }
    }
}
