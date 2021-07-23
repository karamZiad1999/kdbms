package database;

import SQL.Select;

import java.io.PrintWriter;

public class SelectionManager {
    Table table;
    String field;
    String condition;
    String value;
    PrintWriter out;

    public SelectionManager(Table table, String field, String condition, String value, PrintWriter out){
        this.field = field;
        this.condition = condition;
        this.value = value;
        this.table = table;
        this.out = out;
    }

    public SelectionManager(Table table, PrintWriter out){
        this.table = table;
        this.out = out;
    }

    public void select(){
        if(table.isFieldPrimaryKey(field)) selectUsingPrimaryKey();
        else selectUsingCondition();
    }

    public void selectAll(){
        RecordIterator recordIterator = table.getRecordIterator();
        Record record;
        while(recordIterator.hasNext()){
            record = recordIterator.getNextRecord();
            System.out.println(record.printRecord());
        }
    }

    private void selectUsingPrimaryKey(){
        Record record = table.getRecord(value);
//        out.println(record.printHeader());
//        out.println(record.printRecord());

        System.out.println(record.printRecord());
    }

    private void selectUsingCondition(){
        RecordIterator recordIterator = table.getRecordIterator();
        Record record;

        while(recordIterator.hasNext()){
            record = recordIterator.getNextRecord();
            if(record.checkCondition(field, condition, value)){
                System.out.println(record.printRecord());
            }
        }
    }


}
