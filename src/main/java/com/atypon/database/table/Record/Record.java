package com.atypon.database.table.Record;

import com.atypon.database.table.Record.Field.Field;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Record{

    private LinkedHashMap<String, Field> fields;
    String primaryKey;

    public Record(LinkedHashMap<String, Field> fields, String primaryKey){
        this.fields = fields;
        this.primaryKey = primaryKey;
    }

    public Field getField(String name){return fields.get(name);}


    public String getPrimaryKey(){
        return primaryKey;
    }

    public void addRecord(String record) {
       String [] recordSegments = record.split("\n");

        int i = 0;
        for (Map.Entry<String, Field> entry : fields.entrySet()) {
                entry.getValue().setValue(recordSegments[i++]);
        }
    }

    public boolean checkCondition(String field, String condition, String value){
        return getField(field).checkCondition(condition, value);
    }

    public void updateRecord(HashMap<String, String> updates){
        for(Map.Entry<String,String> update : updates.entrySet()){
            fields.get(update.getKey()).setValue(update.getValue());
        }
    }

    public String getRecordBlock(){
        StringBuilder block = new StringBuilder();
        for(Map.Entry<String,Field> entry : fields.entrySet()){
            block.append(entry.getValue().getValue());
            block.append("\n");
        }
        if (block.length() > 0 ) block.deleteCharAt(block.length() - 1);
        return block.toString();
    }

    public String printRecord(){
        StringBuilder record = new StringBuilder();
        for(Map.Entry<String, Field> entry : fields.entrySet() ){
            record.append(entry.getKey());
            record.append(" : ");
            record.append(entry.getValue().getValue());
            record.append(" |");
        }
        record.append("\n");
        return record.toString();
    }


}
