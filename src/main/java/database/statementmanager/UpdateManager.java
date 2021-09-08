package database.statementmanager;

import database.table.Record.LockableIndex;
import database.table.Record.Record;
import database.table.RecordIterator;
import database.table.Table;
import database.table.UpdatableTable;
import SQL.Statement.UpdateStatement;
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

    public UpdateManager(UpdateStatement update, Table table){
        field = update.getField();
        condition = update.getCondition();
        value = update.getValue();
        updates = update.getUpdates();
        this.table = table;
        out = update.getOutputStream();
        recordIndexList = new LinkedList<LockableIndex>();
    }

    public void execute(){
        if(table.isFieldPrimaryKey(field)) updateUsingPrimaryKey();
        else updateUsingCondition();
        if(out != null) out.println("Transaction Successful\n");
    }

    private void updateUsingPrimaryKey(){
        Record record = table.getRecord(value);
        if(record != null) {
            record.updateRecord(updates);
            table.updateRecord(value, record.getRecordBlock());
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
            table.updateRecord(record.getPrimaryKey(), record.getRecordBlock());
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