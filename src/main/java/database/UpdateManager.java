package database;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UpdateManager{
    String field;
    String condition;
    String value;
    HashMap<String,String> updates;
    Table table;

    public UpdateManager(Table table, String field, String condition, String value, HashMap<String,String> updates){
        this.field = field;
        this.condition = condition;
        this.value = value;
        this.updates = updates;
        this.table = table;
    }

    public void update(){
        if(table.isFieldPrimaryKey(field)) updateUsingPrimaryKey();
        else updateUsingCondition();
    }

    private void updateUsingPrimaryKey(){
        Record record = table.getRecord(value);
        if(record != null) record.updateRecord(updates);
        table.updateRecord(value, record.getRecordBlock());
    }

    private void updateUsingCondition(){
        RecordIterator recordIterator = table.getRecordIterator();
        Record record;
        while(recordIterator.hasNext()){
            record = recordIterator.getNextRecord();
            if(record == null) continue;
            if(record.checkCondition(field, condition, value)) {
                record.updateRecord(updates);
                table.updateRecord(record.getPrimaryKey(), record.getRecordBlock());
            }
        }
    }
}
