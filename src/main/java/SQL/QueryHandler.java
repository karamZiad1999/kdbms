package SQL;
import Database.KDBMS;

import java.io.PrintWriter;
import SQL.Statement.*;
public class QueryHandler {

    KDBMS database;
    PrintWriter out;
    QueryLog queryLog;

    public QueryHandler(PrintWriter out){
        database = KDBMS.getInstance();
        this.out = out;
        queryLog = QueryLog.getInstance();
    }

    public void handleQuery(String query){
        Statement statement = StatementFactory.makeStatement(query);
        statement.setOutputStream(out);

        queryLog.logQuery(this.toString(), query);
        execute(statement);
        queryLog.markQueryExecuted(this.toString(), query);
    }

    public void execute(Statement statement){
        database.execute(statement);
    }
}
