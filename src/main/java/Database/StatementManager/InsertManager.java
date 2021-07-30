package Database.StatementManager;

import Database.Table.InsertableTable;
import Database.Table.Table;
import SQL.Statement.InsertStatement;

import java.io.PrintWriter;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class InsertManager implements StatementManager {
    String record;
    String primaryKey;
    InsertableTable table;
    PrintWriter out;

    public InsertManager(InsertStatement insert, Table table) {
        record = insert.getRecordBlock();
        primaryKey = insert.getPrimaryKey();
        this.table = table;
        out = insert.getOutputStream();
    }
    public void execute(){
        WriteLock lock = table.getLock().writeLock();
        try{
            lock.lock();
            table.insertRecord(primaryKey, record);
        }finally{
            lock.unlock();
        }

        if(out != null) out.println("Transaction Successful\n");
    }
}
