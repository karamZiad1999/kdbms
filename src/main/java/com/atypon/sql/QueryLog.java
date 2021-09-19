package com.atypon.sql;

import com.atypon.database.KDBMS;
import com.atypon.sql.statement.Statement;
import com.atypon.sql.statement.StatementFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class QueryLog {
    File queryLogSrc;
    private static QueryLog queryLog;
    private ReentrantReadWriteLock lock;
    private HashMap<String, Long> queryLogIndexMap;
    private KDBMS database;

    private QueryLog() {
        createQueryLogSrc();
        lock = new ReentrantReadWriteLock();
        queryLogIndexMap = new HashMap<String, Long>();
        database = KDBMS.getInstance();
        executeFailedQueries();
    }

    public static QueryLog getInstance(){
        if(queryLog == null) queryLog = new QueryLog();
        return queryLog;
    }

    private void createQueryLogSrc(){
        try{
            queryLogSrc = new File("qeuryLog.kdb");
            queryLogSrc.createNewFile();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void logQuery(String classInfo, String query){
        try{
            lock.writeLock().lock();

            String key = classInfo + " " + query;
            query = " " + query + "\n";
            long byteOffset = queryLogSrc.length();
            printToSrc(query, byteOffset);
            queryLogIndexMap.put(key, byteOffset);
        }finally{
            lock.writeLock().unlock();
        }
    }

    public void markQueryExecuted(String classInfo, String query){
        try{
            lock.writeLock().lock();

            String key = classInfo + " " + query;
            if(queryLogIndexMap.containsKey(key)){
                long byteOffset = queryLogIndexMap.remove(key);
                printToSrc("*", byteOffset);
            }else{
                System.out.println("key not found");
                return;
            }
        }finally{
            lock.writeLock().unlock();
        }
    }

    private void printToSrc(String logEntry, long byteOffset){
        try(RandomAccessFile queryLogWriter = new RandomAccessFile(queryLogSrc , "rw")){
            byte [] logEntryBytes = logEntry.getBytes();
            queryLogWriter.seek(byteOffset);
            queryLogWriter.write(logEntryBytes);
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public String getQueryLog(){
        String queryLog = "";
        try(RandomAccessFile queryLogWriter = new RandomAccessFile(queryLogSrc , "rw")){

            byte [] queryLogBytes = new byte[(int)queryLogSrc.length()] ;
            queryLogWriter.seek(0);
            queryLogWriter.read(queryLogBytes);
            queryLog = new String(queryLogBytes, StandardCharsets.UTF_8);
        }catch (IOException e){
            System.out.println(e);
        }
        return queryLog;
    }

    private void executeFailedQueries(){
        try{
            lock.writeLock().lock();
            String queryLog = getQueryLog();
            if(queryLog.length() == 0) return;
            String [] queries = queryLog.split("\n");

            for(String query : queries){
                char firstChar = query.charAt(0);
                if(firstChar != '*') resendQuery(query);
            }
            deleteQueryLog();
        }finally {
            lock.writeLock().unlock();
        }
    }

    private void deleteQueryLog(){
        try(RandomAccessFile queryLogWriter = new RandomAccessFile(queryLogSrc , "rw")){
            queryLogWriter.setLength(0);
        }catch(IOException e){
            System.out.println(e);
        }
    }
    public void resendQuery(String query){
        Statement statement = StatementFactory.makeStatement(query);
        database.execute(statement);
    }
}
