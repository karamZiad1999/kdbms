package SQL.Statement;

import Database.StatementManager.StatementManager;

public interface InsertStatement extends Statement {
    public String getRecordBlock();
    public String getPrimaryKey();
}
