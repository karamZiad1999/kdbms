package SQL;
import Database.KDBMS;

import java.io.PrintWriter;
import SQL.Statement.*;
public class QueryHandler {

    KDBMS database;
    PrintWriter out;
    QueryLog queryLog;
    Authorization authorization;

    public QueryHandler(PrintWriter out, Authorization authorization){
        database = KDBMS.getInstance();
        this.out = out;
        queryLog = QueryLog.getInstance();
        this.authorization = authorization;
    }

    public void handleQuery(String query){
        Statement statement = StatementFactory.makeStatement(query);
        statement.setOutputStream(out);
        try{
            queryLog.logQuery(this.toString(), query);
            database.execute(statement, authorization);
        }finally {
            queryLog.markQueryExecuted(this.toString(), query);
        }
    }
}
