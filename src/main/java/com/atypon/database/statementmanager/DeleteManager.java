package com.atypon.database.statementmanager;

import com.atypon.database.table.DeletableTable;
import com.atypon.database.table.Record.LockableIndex;
import com.atypon.database.table.Record.Record;
import com.atypon.database.table.RecordIterator;
import com.atypon.database.table.Table;
import com.atypon.SQL.Statement.DeleteStatement;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DeleteManager implements StatementManager{
    DeletableTable table;
    String field;
    String condition;
    String value;
    PrintWriter out;
    LinkedList<LockableIndex> records;
    private LinkedList<LockableIndex> recordIndexList;

    public DeleteManager(DeleteStatement delete, Table table){
        this.table = table;
        field = delete.getField();
        condition = delete.getCondition();
        value = delete.getValue();
        out = delete.getOutputStream();
        records = new LinkedList<LockableIndex>();
        recordIndexList = new LinkedList<LockableIndex>();
    }

    public void execute(){
        if(table.isFieldPrimaryKey(field)) deleteUsingPrimaryKey();
        else deleteUsingCondition();
        if(out != null ) out.println("Transaction Successful\n");
    }

    private void deleteUsingPrimaryKey(){
        LockableIndex index = table.getRecordInfo(value);
        try{
            index.writeLock();
            table.deleteRecord(value);

        }finally {
            index.writeUnlock();
        }
    }

    private void deleteUsingCondition(){
        try{
            acquireLocks();
            deleteLockedRecords();
        }finally {
            releaseLocks();
        }
    }

    private void deleteLockedRecords(){
        try {
            table.getLock().writeLock().lock();

            for (LockableIndex lockableIndex : recordIndexList) {
                Record record = table.getRecord(lockableIndex.getPrimaryKey());
                table.deleteRecord(record.getPrimaryKey());
            }
        }finally {
            table.getLock().writeLock().unlock();
        }
    }
    private void releaseLocks(){
        while(!recordIndexList.isEmpty()){
            LockableIndex lockableIndex = recordIndexList.pop();
            lockableIndex.writeUnlock();
        }
    }

    private void acquireLocks(){
        RecordIterator recordIterator = table.getRecordIterator();

        while (recordIterator.hasNext()) {
            Record record = recordIterator.getNextRecord();
            if(record.checkCondition(field, condition, value)){
                LockableIndex index = table.getRecordInfo(record.getPrimaryKey());
                ReentrantReadWriteLock.WriteLock lock = index.getLock().writeLock();
                try{
                    if(lock.tryLock(120, TimeUnit.SECONDS)){
                        recordIndexList.add(index);
                    }else{
                        releaseLocks();
                        deleteUsingCondition();
                    }
                }catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
    }
}

