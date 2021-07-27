package Database.StatementManager;

import Database.Table.Record.Record;
import Database.Table.RecordIterator;
import Database.Table.Table;
import SQL.Statement.UpdateStatement;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UpdateManager implements StatementManager{
    String field;
    String condition;
    String value;
    HashMap<String,String> updates;
    Table table;
    Stack<ReentrantReadWriteLock> locks;
    PrintWriter out;

    public UpdateManager(UpdateStatement update, Table table) {
        field = update.getField();
        condition = update.getCondition();
        value = update.getValue();
        updates = update.getUpdates();
        table = table;
        locks = new Stack<ReentrantReadWriteLock>();
        out = update.getOutputStream();
    }

    public void execute(){
        if(table.isFieldPrimaryKey(field)) updateUsingPrimaryKey();
        else updateUsingCondition();
        if(out != null) out.println("Transaction Successful\n");
    }

    private void updateUsingPrimaryKey(){
        updateUsingPrimaryKey(value);
    }
    private void updateUsingPrimaryKey(String primaryKey){
        Record record = table.getRecord(primaryKey);
        if(record != null) record.updateRecord(updates);
        table.updateRecord(primaryKey, record.getRecordBlock());
    }

    private void updateUsingCondition(){
        ArrayList<String> matchingRecords = table.getRecordsWithCondition(field, condition, value);
        if(!acquireLocks(matchingRecords)) return;

        for(String primaryKey : matchingRecords){
            {
                updateUsingPrimaryKey(primaryKey);
            }
        }
    }

    private boolean acquireLocks(ArrayList<String> primaryKeys){
        for(String primaryKey : primaryKeys){
           acquireLock(primaryKey);
        }
        return true;
    }

    private void releaseLocks(){
        while(!locks.isEmpty()){
            locks.pop().writeLock().unlock();
        }
    }

    private void acquireLock(String primaryKey){
        ReentrantReadWriteLock lock = table.getRecordInfo(primaryKey).getLock();
        if(lock.writeLock().tryLock()) {
            locks.push(lock);
        }
    }
}
