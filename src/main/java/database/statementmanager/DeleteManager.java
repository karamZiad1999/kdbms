package database.statementmanager;

import database.table.DeletableTable;
import database.table.Record.LockableIndex;
import database.table.Record.Record;
import database.table.RecordIterator;
import database.table.Table;
import SQL.Statement.DeleteStatement;
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
        table.deleteRecord(value);
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

