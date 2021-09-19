package com.atypon.relation;

import com.atypon.database.table.Record.Record;
import com.atypon.database.table.Table;

import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RelationManager{

    String schemaName;
    private Set<String> tableSet;
    private HashMap<String, Relation> relations;
    private HashMap<String, Table> tables;

    public RelationManager(String schemaName){
        this.schemaName = schemaName;
        tableSet= new HashSet<String>();
        tables= new HashMap<String, Table>();
        relations = new HashMap<String,Relation> ();
        initializeTableSet();
    }

    public void initializeTableSet(){
        try(RandomAccessFile tableSetSrc = new RandomAccessFile(schemaName + "/tableSet.kdb", "rw");){
            String tableName;
            while((tableName = tableSetSrc.readLine()) != null) tableSet.add(tableName);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void addRelation(String tableName, String fieldName, Relation relation){
        String key = tableName + "." + fieldName;
        relations.put(key, relation);
    }

    public String getRelation(String table, String field, String primaryKey){
        String key = table + "." + field;
        Relation relation = relations.get(key);
        String relationName=relation.getRelationName();
        String relationFieldName = relation.getFieldName();

        Table relatedTable = fetchTable(relationName);
        Record record = relatedTable.getRecord(primaryKey);
        String output ;
        if(record==null) output = "NaN";
        else output = record.getField(relationFieldName).getValue();
        return output;
    }

    public void addTable(String tableName){
        tableSet.add(tableName);

        try(FileWriter tableSetWriter = new FileWriter( schemaName + "/tableSet.kdb", true)){
            tableSetWriter.write(tableName + "\n");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Table fetchTable(String tableName){
        if(tableSet.contains(tableName) && tables.get(tableName)==null) tables.put(tableName, new Table(schemaName, tableName ));
        return tables.get(tableName);
    }
}
