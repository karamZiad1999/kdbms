//package database;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//public class UpdateManager{
//
//
//    String primaryKeyField;
//    String field;
//    String condition;
//    String value;
//    HashMap<String,String> updates;
//
//    public UpdateManager(String field, String condition, String value, HashMap<String,String> updates){
//        this.field = field;
//        this.condition = condition;
//        this.value = value;
//        primaryKeyField = recordFactory.getPrimaryKey();
//        this.updates = updates;
//    }
//
//    public void updateIfFound(){
//        if(isFieldPrimaryKey()) updateUsingPrimaryKey();
//        else updateUsingCondition();
//    }
//
//    private boolean isFieldPrimaryKey(){return field.equalsIgnoreCase(primaryKeyField);}
//
//    private void updateUsingPrimaryKey(){
//        cacheRecord(value);
//        Record record = cache.getRecord(value);
//        record.updateRecord(updates);
//        tableManager.printToFile(record.getRecordBlock().getBytes());
//    }
//
//    private BlockInfo getBlockInfo(){return indexMap.remove(value);}
//
//    private void updateUsingCondition(){
//        Iterator recordIterator = indexMap.entrySet().iterator();
//
//        while (recordIterator.hasNext()) {
//            Map.Entry entry = (Map.Entry)recordIterator.next();
//            String primaryKey = (String) entry.getKey();
//            cacheRecord(primaryKey);
//            Record record = cache.getRecord(primaryKey);
//
//            if(hasInstance(record)) updateIfMatch(record, recordIterator, primaryKey);
//        }
//    }
//
//    private void updateIfMatch(Record record, Iterator recordIterator, String primaryKey){
//        if(conditionApplies(record , field, condition, value)){
//            record.updateRecord(updates);
//        }
//    }
//
//    private boolean hasInstance(Object obj){return (obj != null);}
//}
