package com.atypon.database;

import com.atypon.relation.RelationManager;
import com.atypon.relation.RelationManagerFactory;
import com.atypon.database.table.Table;

public class Schema {
    private RelationManager relationManager;
    private String schemaName;


    public Schema(String schemaName){
        this.schemaName=schemaName;
        System.out.println("schema: "+ schemaName);
        relationManager = RelationManagerFactory.getRelationManager(schemaName);
    }


    public void addTable(String tableName){
       relationManager.addTable(tableName);
    }

    public Table fetchTable(String tableName){
        return relationManager.fetchTable(tableName);
    }


}
