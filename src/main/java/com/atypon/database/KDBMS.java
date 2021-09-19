package com.atypon.database;

import java.io.*;
import java.util.*;

import com.atypon.database.statementmanager.StatementManager;
import com.atypon.database.statementmanager.StatementManagerFactory;
import com.atypon.sql.statement.Statement;


public class KDBMS {

    private static KDBMS database;
    private HashMap<String, Schema> schemas;
    private Set<String> schemaSet;

    private KDBMS(){
        schemas = new HashMap<String, Schema>();
        schemaSet = new HashSet<String>();
        initializeSchemaSet();

    }

    public static KDBMS getInstance(){
        if(database == null) database = new KDBMS();
        return database;
    }

    public void addSchema(String schemaName){
        schemaSet.add(schemaName);

        try(FileWriter schemaSetWriter = new FileWriter("schemaSet.kdb", true)){
            schemaSetWriter.write(schemaName + "\n");
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void execute(Statement statement){
        Schema schema = fetchSchema(statement.getSchemaName());
        StatementManager statementManager = StatementManagerFactory.makeStatementManager(statement, schema);
        if(statementManager!=null) statementManager.execute();
    }

    public Schema fetchSchema(String schemaName){
        if(schemaSet.contains(schemaName) && schemas.get(schemaName)==null) schemas.put(schemaName, new Schema(schemaName));
        return schemas.get(schemaName);
    }

    public void initializeSchemaSet(){
        try(RandomAccessFile schemaSetSrc = new RandomAccessFile("schemaSet.kdb", "rw");){
            String schemaName;
            while((schemaName = schemaSetSrc.readLine()) != null) schemaSet.add(schemaName);
        }catch(IOException e){
            System.out.println(e);
        }
    }
    public boolean isSchemaNameLegal(String schemaName){
        return (!schemaSet.contains(schemaName));
    }
}
