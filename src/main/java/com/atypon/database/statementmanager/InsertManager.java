package com.atypon.database.statementmanager;

import com.atypon.database.Schema;
import com.atypon.database.table.InsertableTable;
import com.atypon.database.table.Record.Record;
import com.atypon.database.table.Record.RecordFactory;
import com.atypon.database.table.Table;
import com.atypon.sql.statement.InsertStatement;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class InsertManager implements StatementManager {
    ArrayList<String> valueList;
    Schema schema;
    InsertableTable table;
    PrintWriter out;
    RecordFactory factory;
    public InsertManager(InsertStatement insert, Schema schema) {
        out = insert.getOutputStream();
        this.schema=schema;
        this.table = schema.fetchTable(insert.getTableName());
        this.valueList=insert.getValueList();
        this.factory=table.getRecordFactory();
    }

    public void execute(){
        WriteLock lock = table.getLock().writeLock();
        try{
            lock.lock();
            Record record = factory.createRecord(valueList);
            table.insertRecord(record);
        }finally{
            lock.unlock();
        }

        if(out != null) out.println("Transaction Successful\n");
    }

}
