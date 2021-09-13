package com.atypon.database.statementmanager;

import com.atypon.database.KDBMS;
import com.atypon.database.table.Table;
import com.atypon.SQL.Statement.CreateStatement;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class CreateManager implements StatementManager {

    String tableName;
    String primaryKey;
    String metaData;
    PrintWriter out;

    public CreateManager(CreateStatement create, Table table) {
        tableName = create.getTableName();
        primaryKey = create.getPrimaryKey();
        metaData = create.getMetaDataInString();
        out = create.getOutputStream();
    }

    public void execute(){
        System.out.println("executing print table");
        KDBMS database = KDBMS.getInstance();
        database.addTable(tableName);

        try{
            createTableSrc();
            createIndexSrc();
            createMetaDataSrc();
            printMetaData();
        }catch (IOException e){
            System.out.println(e);
        }
        if(out != null) out.println("Transaction Successful\nend");
    }

    public void createTableSrc() throws IOException{
        System.out.println("creating table src");
        File tableSrc = new File(tableName + ".kdb");
        tableSrc.createNewFile();
    }

    public void createIndexSrc() throws IOException{
        File indexSrc  = new File(tableName + "-index.kdb");
        indexSrc.createNewFile();
    }

    public void createMetaDataSrc() throws IOException{
        File metaDataSrc = new File(tableName + "-metaData.kdb");
        metaDataSrc.createNewFile();
    }

    public void printMetaData(){
        try(FileWriter fileWriter = new FileWriter(tableName + "-metaData.kdb", true)){
            fileWriter.write(metaData);
        }catch(IOException e){
            System.out.println(e);
        }
    }



}
