package Database.StatementManager;

import Database.Table.Table;
import SQL.Statement.InsertStatement;

import java.io.PrintWriter;

public class InsertManager implements StatementManager {
    String record;
    String primaryKey;
    Table table;
    PrintWriter out;

    public InsertManager(InsertStatement insert, Table table) {
        record = insert.getRecordBlock();
        primaryKey = insert.getPrimaryKey();
        this.table = table;
        out = insert.getOutputStream();
    }
    public void execute(){
        table.insertRecord(primaryKey, record);
        if(out != null) out.println("Transaction Successful\n");
    }
}
