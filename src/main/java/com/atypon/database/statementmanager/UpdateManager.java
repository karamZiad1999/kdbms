package com.atypon.database.statementmanager;

import com.atypon.database.Schema;
import com.atypon.database.table.Record.LockableIndex;
import com.atypon.database.table.Record.Record;
import com.atypon.database.table.RecordIterator;
import com.atypon.database.table.Table;
import com.atypon.database.table.UpdatableTable;
import com.atypon.sql.statement.UpdateStatement;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UpdateManager implements StatementManager {
    String field;
    String condition;
    String value;
    HashMap<String, String> updates;
    UpdatableTable table;
    PrintWriter out;
    LinkedList<LockableIndex> recordIndexList;
    Schema schema;

    public UpdateManager(UpdateStatement update, Schema schema){
        this.schema = schema;
        field = update.getField();
        condition = update.getCondition();
        value = update.getValue();
        updates = update.getUpdates();
        this.table = schema.fetchTable(update.getTableName());
        out = update.getOutputStream();
        recordIndexList = new LinkedList<LockableIndex>();
    }

    public void execute(){
        if(table.isFieldPrimaryKey(field)) updateUsingPrimaryKey();
        else updateUsingCondition();
        if(out != null) out.println("Transaction Successful\n");
    }

    private void updateUsingPrimaryKey(){
        LockableIndex index = table.getRecordInfo(value);
        Record record = table.getRecord(value);
        try{
            index.writeLock();
            if(record != null) {
                record.updateRecord(updates);
                table.updateRecord(value, record.printEntry());
            }
        }finally {
            index.writeUnlock();
        }

    }

    private void updateUsingCondition(){
        try{
            acquireLocks();
            updateLockedRecords();
        }finally {
            releaseLocks();
        }
    }

    private void updateLockedRecords(){
        for(LockableIndex lockableIndex : recordIndexList){
            Record record = table.getRecord(lockableIndex.getPrimaryKey());
            record.updateRecord(updates);
            table.updateRecord(record.getPrimaryKey(), record.printEntry());
        }

    }
    private void releaseLocks(){
      while(!recordIndexList.isEmpty()){
          LockableIndex lockableIndex = recordIndexList.pop();
          lockableIndex.writeUnlock();
      }
    }

    private void acquireLocks() {
        try {
            table.getLock().readLock().lock();
            RecordIterator recordIterator = table.getRecordIterator();

            while (recordIterator.hasNext()) {
                Record record = recordIterator.getNextRecord();
                if (record.checkCondition(field, condition, value)) {
                    LockableIndex index = table.getRecordInfo(record.getPrimaryKey());
                    ReentrantReadWriteLock.WriteLock lock = index.getLock().writeLock();
                    try {
                        if (lock.tryLock(120, TimeUnit.SECONDS)) {
                            recordIndexList.add(index);
                        } else {
                            releaseLocks();
                            updateUsingCondition();
                        }
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
            }
        }finally{
            table.getLock().readLock().unlock();
        }
    }
}