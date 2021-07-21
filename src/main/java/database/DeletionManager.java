package database;

public class DeletionManager {
    Table table;
    String field;
    String condition;
    String value;

    public DeletionManager(Table table, String field, String condition, String value){
        this.field = field;
        this.condition = condition;
        this.value = value;
        this.table = table;
    }

    public void delete(){
        if(table.isFieldPrimaryKey(field)) deleteUsingPrimaryKey();
        else deleteUsingCondition();
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
            if(record.checkCondition(field, condition, value)) {
                table.deleteRecord(record.getPrimaryKey());
                recordIterator.deleteRecord();
            }
        }
    }
}

