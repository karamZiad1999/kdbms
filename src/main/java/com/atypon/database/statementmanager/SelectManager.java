package com.atypon.database.statementmanager;

import com.atypon.database.table.Record.LockableIndex;
import com.atypon.database.table.Record.Record;
import com.atypon.database.table.RecordIterator;
import com.atypon.database.table.SelectableTable;
import com.atypon.database.table.Table;
import com.atypon.SQL.Statement.SelectStatement;

import java.io.PrintWriter;
import java.util.Scanner;

public class SelectManager implements StatementManager {
    private SelectableTable table;
    private String field;
    private String condition;
    private String value;
    private PrintWriter out;
    private boolean isCondition;

    public SelectManager(SelectStatement select, Table table) {
        this.table = table;
        this.field = select.getField();
        this.condition = select.getCondition();
        this.value = select.getValue();
        this.out = select.getOutputStream();
        this.isCondition = select.isCondition();
    }
    public void execute(){
        if(isCondition) select();
        else selectAll();
    }

    public void select(){
        if(table.isFieldPrimaryKey(field)) selectUsingPrimaryKey();
        else selectUsingCondition();
    }

    public void selectAll(){
        try{
            table.getLock().readLock().lock();
            RecordIterator recordIterator = table.getRecordIterator();
            Record record;
            StringBuilder output = new StringBuilder();

            if(recordIterator.hasNext())
            {
                record = recordIterator.getNextRecord();
                output.append(record.getHeader());
                output.append(record.printRecord());
            }

            while(recordIterator.hasNext()){
                record = recordIterator.getNextRecord();
                LockableIndex recordLock = table.getRecordInfo(record.getPrimaryKey());
                try{
                    recordLock.readLock();
                    output.append(record.printRecord());
                }finally{
                    recordLock.readUnlock();
                }
            }
            output.append("end");
            printOutput(output.toString());
        }finally{
            table.getLock().readLock().unlock();
        }
    }

    private void selectUsingPrimaryKey(){
        LockableIndex recordLock= table.getRecordInfo(value);
        recordLock.readLock();
        Record record = table.getRecord(value);
        recordLock.readUnlock();
        printOutput(record.printRecord());
    }

    private void selectUsingCondition(){
        try{
            table.getLock().readLock().lock();
        RecordIterator recordIterator = table.getRecordIterator();
        StringBuilder output = new StringBuilder();

        while (recordIterator.hasNext()) {
            Record record = recordIterator.getNextRecord();
            LockableIndex recordLock = table.getRecordInfo(record.getPrimaryKey());
            try{
                recordLock.readLock();
                if (record.checkCondition(field, condition, value)) output.append(record.printRecord());
            }finally{
                recordLock.readUnlock();
            }

        }
        printOutput(output.toString());
        }finally {
            table.getLock().readLock().unlock();
        }
    }

    private void printOutput(String output){
        if(out != null){
            if(output.length() > 0){
                out.println(output);
            }
            else out.println("no values found\n");
        }
    }
}
