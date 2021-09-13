package com.atypon.database;

import java.io.*;
import java.util.*;
import com.atypon.database.statementmanager.StatementManager;
import com.atypon.database.statementmanager.StatementManagerFactory;
import com.atypon.database.table.Table;
import com.atypon.SQL.Statement.Statement;


public class KDBMS {

    private static KDBMS database;
    private HashMap<String, Table> tables;
    private Set<String> tableSet;

    private KDBMS(){
        tables = new HashMap<String, Table>();
        tableSet = new HashSet<String>();
        initializeTableSet();

    }

    public static KDBMS getInstance(){
        if(database == null) database = new KDBMS();
        return database;
    }

    public void addTable(String tableName){
        tableSet.add(tableName);
        tables.put(tableName, new Table(tableName));

        try(FileWriter tableSetWriter = new FileWriter("tableSet.kdb", true)){
            tableSetWriter.write(tableName + "\n");
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void execute(Statement statement){
        Table table = fetchTable(statement.getTableName());
        if(table == null) return;
        StatementManager statementManager = StatementManagerFactory.makeStatementManager(statement, table);
        statementManager.execute();
    }

    public Table fetchTable(String tableName){
        if(tableSet.contains(tableName) && tables.get(tableName) == null) tables.put(tableName, new Table(tableName));
        return tables.get(tableName);
    }

    public void initializeTableSet(){
        try(RandomAccessFile tableSetSrc = new RandomAccessFile("tableSet.kdb", "rw");){
            String tableName;
            while((tableName = tableSetSrc.readLine()) != null) tableSet.add(tableName);
        }catch(IOException e){
            System.out.println(e);
        }
    }
    public boolean isTableNameLegal(String tableName){
        return (!tableSet.contains(tableName));
    }
}
