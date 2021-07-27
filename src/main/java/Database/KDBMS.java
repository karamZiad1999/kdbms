package Database;

import java.io.*;
import java.util.*;
import Database.StatementManager.StatementManager;
import Database.StatementManager.StatementManagerFactory;
import Database.Table.Table;
import SQL.QueryLog;
import SQL.Statement.Statement;


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
