package com.atypon.database.statementmanager;

import com.atypon.database.KDBMS;
import com.atypon.database.Schema;
import com.atypon.sql.statement.CreateStatement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class CreateManager implements StatementManager {


    private String schemaName;
    private String tableName;
    private String metaData;
    private PrintWriter out;
    private KDBMS database;
    private boolean isCreateSchema;

    public CreateManager(CreateStatement create) {
        database = KDBMS.getInstance();
        this.out = create.getOutputStream();
        isCreateSchema = create.isCreateSchema();
        if(isCreateSchema) initializeCreateSchema(create);
        else initializeCreateTable(create);
    }

    public void initializeCreateSchema(CreateStatement create){
        schemaName = create.getSchemaName();
    }

    public void initializeCreateTable(CreateStatement create){
        schemaName = create.getSchemaName();
        tableName = create.getTableName();
        metaData = create.getMetaDataInString();
        out = create.getOutputStream();
    }


    public void execute(){
        if(isCreateSchema) createSchema();
        else createTable();
    }

    private void createSchema(){
        database.addSchema(schemaName);
        createSchemaDirectory();
    }

    private void createSchemaDirectory(){
        new File(schemaName).mkdirs();
    }


    private void createTable(){
        Schema schema = database.fetchSchema(schemaName);
        try{
            createTableSrc();
            createIndexSrc();
            createMetaDataSrc();
            printMetaData();
            if(schema != null) schema.addTable(tableName);
        }catch (IOException e){
           e.printStackTrace();
        }

        if(out != null) out.println("Transaction Successful\nend");
    }

    public void createTableSrc() throws IOException{
        File tableSrc = new File( schemaName + "/"+ tableName + ".kdb");
        tableSrc.createNewFile();
    }

    public void createIndexSrc() throws IOException{
        File indexSrc  = new File(schemaName + "/"+tableName + "-index.kdb");
        indexSrc.createNewFile();
    }

    public void createMetaDataSrc() throws IOException{
        File metaDataSrc = new File(schemaName + "/"+tableName + "-metaData.kdb");
        metaDataSrc.createNewFile();
    }

    public void printMetaData(){
        try(FileWriter fileWriter = new FileWriter(schemaName + "/"+ tableName + "-metaData.kdb", true)){
            fileWriter.write(metaData);
        }catch(IOException e){
            System.out.println(e);
        }
    }



}
