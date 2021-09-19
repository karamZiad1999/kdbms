package com.atypon.sql;
import com.atypon.database.KDBMS;

import java.io.PrintWriter;
import com.atypon.sql.statement.*;
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
        try{
            queryLog.logQuery(this.toString(), query);
            database.execute(statement);
        }finally{
            queryLog.markQueryExecuted(this.toString(), query);
        }
    }
}
