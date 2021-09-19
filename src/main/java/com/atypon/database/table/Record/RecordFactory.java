package com.atypon.database.table.Record;

import com.atypon.relation.Relation;
import com.atypon.relation.RelationManager;
import com.atypon.relation.RelationManagerFactory;
import com.atypon.database.table.Record.Field.Field;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class RecordFactory {
    String schemaName;
    String tableName;
    private LinkedHashMap<String, Field> fieldsTemplate;
    File metaDataSrc;
    String primaryKey;

    public RecordFactory( String schemaName , String tableName){
        this.schemaName = schemaName;
        this.tableName = tableName;
        metaDataSrc = new File(schemaName + "/" + tableName + "-metaData.kdb");
        fieldsTemplate = new LinkedHashMap<String,Field>();
        initializeFields();
    }

    private void initializeFields(){
        String line;
        String [] fieldMetaData;

        try(RandomAccessFile metaDataSrcReader = new RandomAccessFile(metaDataSrc, "rw")){
            while((line = metaDataSrcReader.readLine())!= null){
                fieldMetaData = line.split(":");
                fieldsTemplate.put(fieldMetaData[0], new Field(fieldMetaData[1]));
                if(fieldMetaData[1].equalsIgnoreCase("relation")) initializeRelation(fieldMetaData);
                else if(fieldMetaData.length==3) primaryKey=fieldMetaData[0];
            }
            if(primaryKey==null) setDefaultPrimaryKey();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void setDefaultPrimaryKey(){
        primaryKey=fieldsTemplate.entrySet().iterator().next().getKey();
    }

    private void initializeRelation(String[] fieldMetaData){
        String  fieldName = fieldMetaData[0];
        Field field = fieldsTemplate.get(fieldName);
        field.setSchema(schemaName);
        field.setTable(tableName);
        field.setName(fieldName);
        RelationManager relationManager = RelationManagerFactory.getRelationManager(schemaName);
        String relationName = fieldMetaData[2];
        String relationFieldName = fieldMetaData[3];
        Relation relation = new Relation(relationName, relationFieldName);
        relationManager.addRelation(tableName, fieldName, relation);
    }

    public LinkedHashMap<String, Field> copyFieldsTemplate(){
        LinkedHashMap<String, Field> clone = new LinkedHashMap<String, Field>();

        for(Map.Entry<String, Field> entry : fieldsTemplate.entrySet()){
            clone.put(entry.getKey(), entry.getValue().clone());
        }
        return clone;
    }

    public Record createRecord(ArrayList<String> valueList){
        LinkedHashMap<String, Field> fields = copyFieldsTemplate();
        Iterator fieldsIterator = fields.entrySet().iterator();

        for(String value:valueList){
            if(fieldsIterator.hasNext()){
                Map.Entry<String, Field> entry = (Map.Entry<String, Field>) fieldsIterator.next();
                Field field = entry.getValue();
                field.setValue(value);
            }
        }
        Record record = new Record(fields);
        record.setPrimaryKeyName(primaryKey);
        return record;
    }

    public Record createRecord(String recordBlock){
        String [] recordSegments = recordBlock.split("[,\\s+]");
        LinkedHashMap<String, Field> fields = copyFieldsTemplate();
        Iterator fieldsIterator = fields.entrySet().iterator();

        for(String segment : recordSegments){
            if(fieldsIterator.hasNext()){
                Map.Entry<String, Field> entry = (Map.Entry<String, Field>) fieldsIterator.next();
                Field field = entry.getValue();
                field.setValue(segment);
            }
        }
        Record record = new Record(fields);
        record.setPrimaryKeyName(primaryKey);
        return record;
    }

    public String getPrimaryKey(){return primaryKey;}

    public String getMeta(){
        StringBuilder meta = new StringBuilder();
        for(Map.Entry<String,Field> entry: fieldsTemplate.entrySet()){
            meta.append(entry.getKey());
            meta.append(",");
        }
        meta.append("primary key,");
        meta.append(primaryKey);
        return meta.toString();
    }
}
