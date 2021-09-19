package com.atypon.relation;

public class Relation {
    String relationName;
    String fieldName;

    public Relation(String relationName, String fieldName){
        this.relationName=relationName;
        this.fieldName=fieldName;
    }
    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
