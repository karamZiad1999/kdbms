package com.atypon.database.table.Record.Field;

import com.atypon.relation.RelationManager;
import com.atypon.relation.RelationManagerFactory;
import com.atypon.database.table.Record.Field.ConditionChecker.ConditionChecker;
import com.atypon.database.table.Record.Field.ConditionChecker.ConditionCheckerFactory;

public class Field {

    private String schema = null;
    private String table = null;
    private String name = null;
    private String value = null;
    private String type;

    private ConditionChecker conditionChecker;

    public Field(String type){
        this.type=type;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public void setType(String type){this.type = type;}

    public String getValue(){
        if(type.equalsIgnoreCase("relation")&&table!=null&&schema!=null) return getRelation();
        else return value;
    }

    public String getEntry(){
        return value;
    }

    private String getRelation(){
        RelationManager manager = RelationManagerFactory.getRelationManager(schema);
        return manager.getRelation(table, name, value);
    }

    public String getType(){return type;}

    public boolean checkCondition(String condition, String comparedValue){
        if(conditionChecker==null) conditionChecker= ConditionCheckerFactory.getInstance(type);
        return conditionChecker.checkCondition(value, condition, comparedValue);
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setTable(String table){
        this.table=table;
    }
    public void setName(String name){
        this.name=name;
    }
    @Override
    public Field clone(){
        Field clone = new Field(type);

        if(schema!=null) clone.setSchema(schema);
        if(table!=null) clone.setTable(table);
        if(name!=null) clone.setName(name);
        if(value!=null) clone.setValue(value);

        return clone;
    }
}
