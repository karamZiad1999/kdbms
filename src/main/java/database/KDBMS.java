package database;

import java.io.*;
import java.util.*;


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

    public void getRecord(String tableName, String primaryKey){

        Table temp = tables.get(tableName);
        temp.getRecord(primaryKey);

    }

    public void createTable(String tableName, String metaData){

        File metaDataSrc = null;
        FileWriter metaDataSrcWriter = null;
        try{
            metaDataSrc = new File( tableName + "-metaData.kdb");
            metaDataSrc.createNewFile();
             metaDataSrcWriter = new FileWriter(metaDataSrc);
            metaDataSrcWriter.write(metaData);

        }catch(IOException e){
            System.out.println(e);
        }finally{
            try{
                if(metaDataSrcWriter != null) metaDataSrcWriter.close();
            }catch (IOException e){
                System.out.println(e);
            }

        }
        addTable(tableName);
    }

    public void addToTableSet(String tableName){
        tableSet.add(tableName);

        FileWriter tableSetWriter = null;
        try{
            tableSetWriter = new FileWriter("tableSet.kdb", true);

            tableSetWriter.write(tableName + "\n");

        }catch(IOException e){
            System.out.println(e);
        }finally{
            try{
                if(tableSetWriter != null) tableSetWriter.close();
            }catch (IOException e){
                System.out.println(e);
            }

        }

    }

    public void addTable(String tableName){
        if(tables.get(tableName) != null) return;
        tables.put(tableName, new Table(tableName));
        addToTableSet(tableName);
    }

    public void fetchTable(String tableName){
        if(tables.get(tableName) != null) return;
        tables.put(tableName, new Table(tableName));
    }




    public void insertRecord(String tableName, String primaryKey, String recordData){

        Table table;
        InsertLock insertLock;
        if((table =  tables.get(tableName)) == null ){
            if(tableSet.contains(tableName)) fetchTable(tableName);
            else return;
            table = tables.get(tableName);
        }

        insertLock = new InsertLock(table);

//        insertLock.lock();
        table.insertRecord(primaryKey, recordData);
//        insertLock.unlock();

    }

    public void initializeTableSet(){

                try(RandomAccessFile tableSetSrc = new RandomAccessFile("tableSet.kdb", "rw");)
                {
                    String tableName;
                    while((tableName = tableSetSrc.readLine()) != null) tableSet.add(tableName);


                }catch(IOException e){
                    System.out.println(e);
                }
    }

    public void deleteRecord(String tableName, String field, String condition, String value){

        Table table;

        if((table = tables.get(tableName)) == null ){
            if(tableSet.contains(tableName)) fetchTable(tableName);
            else return;
            table = tables.get(tableName);
        }

        DeletionManager deletionManager = new DeletionManager(table, field, condition, value);
        deletionManager.delete();

    }

//    public void updateRecord(String tableName, String field, String condition, String value, HashMap<String,String> updates){
//
//        Table table;
//
//        if((table = tables.get(tableName)) == null ){
//            if(tableSet.contains(tableName)) fetchTable(tableName);
//            else return;
//            table = tables.get(tableName);
//        }
//
//        table.updateRecord(field, condition, value, updates);
//    }

    public boolean isTableNameLegal(String tableName){
        return (!tableSet.contains(tableName));
    }
}
