package com.atypon.database.table.Record;

import com.atypon.database.table.Record.Field.Field;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Record{

    private LinkedHashMap<String, Field> fields;
    private String primaryKeyName;

    public Record(){}
    public Record(LinkedHashMap<String, Field> fields){
        this.fields = fields;
    }

    public Field getField(String name){return fields.get(name);}


    public String getPrimaryKeyName() {
        return primaryKeyName;
    }
    public void setPrimaryKeyName(String primaryKeyName){this.primaryKeyName=primaryKeyName;}
    public String getPrimaryKey(){
        return getField(primaryKeyName).getValue();
    }
    public boolean checkCondition(String field, String condition, String value){
        return getField(field).checkCondition(condition, value);
    }

    public void updateRecord(HashMap<String, String> updates){
        for(Map.Entry<String,String> update : updates.entrySet()){
            fields.get(update.getKey()).setValue(update.getValue());
        }
    }

    public String printRecord(){
        StringBuilder record = new StringBuilder();
        for(Map.Entry<String, Field> entry : fields.entrySet() ){
            record.append(",");
            record.append(entry.getValue().getValue());
        }
        record.append("\n");
        record.deleteCharAt(0);
        return record.toString();
    }

    public String printEntry(){
        StringBuilder record = new StringBuilder();
        for(Map.Entry<String, Field> entry : fields.entrySet() ){
            record.append(",");
            record.append(entry.getValue().getEntry());
        }
        record.deleteCharAt(0);
        return record.toString();
    }

    public String getHeader(){
        StringBuilder header = new StringBuilder();
        for(Map.Entry<String, Field> entry : fields.entrySet() ){
            header.append(",");
            header.append(entry.getKey());
        }
        header.append("\n");
        header.deleteCharAt(0);
        return header.toString();
    }

    @Override
    public String toString(){
       return printRecord();
    }

}
