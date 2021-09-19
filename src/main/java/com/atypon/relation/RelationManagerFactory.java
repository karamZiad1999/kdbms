package com.atypon.relation;

import java.util.HashMap;

public class RelationManagerFactory{
    private static RelationManagerFactory relationManagerFactory;
    private static HashMap<String, RelationManager> relationManagers;

    private RelationManagerFactory(){
      relationManagers = new HashMap<String, RelationManager>();
    }

    public static RelationManager getRelationManager(String schemaName){
        RelationManager manager = null;
        if(schemaName==null) return manager;
        if(relationManagerFactory==null) relationManagerFactory = new RelationManagerFactory();
        manager = relationManagers.get(schemaName);
        if(manager==null){
            manager = new RelationManager(schemaName);
            relationManagers.put(schemaName, manager);
        }
        return manager;
    }
}
