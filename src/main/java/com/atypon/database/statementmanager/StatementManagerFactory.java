package com.atypon.database.statementmanager;

import com.atypon.database.Schema;
import com.atypon.sql.statement.*;

public class StatementManagerFactory {
    public static StatementManager makeStatementManager(Statement statement, Schema schema){
        StatementManager statementManager = null;
        if(statement instanceof Create){
            statementManager = new CreateManager((Create) statement);
        }
        else if(schema == null){
            return null;
        }
        else if (statement instanceof Insert){
            statementManager =  new InsertManager((Insert) statement, schema);
        }
        else if (statement instanceof Select){
           statementManager =  new SelectManager((Select) statement, schema);
        }
        else if (statement instanceof Update){
            statementManager =  new UpdateManager((Update) statement, schema);
        }
        else if (statement instanceof Delete){
            statementManager =  new DeleteManager((Delete) statement, schema);
        }
        return statementManager;
    }
}
