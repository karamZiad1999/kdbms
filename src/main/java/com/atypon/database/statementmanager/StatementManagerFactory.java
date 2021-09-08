package com.atypon.database.statementmanager;

import com.atypon.database.table.Table;
import com.atypon.SQL.Statement.*;

public class StatementManagerFactory {
    public static StatementManager makeStatementManager(Statement statement, Table table){
        StatementManager statementManager = null;
        if(statement instanceof Create){
            statementManager = new CreateManager((Create) statement, table);
        }
        else if (statement instanceof Insert){
            statementManager =  new InsertManager((Insert) statement, table);
        }
        else if (statement instanceof Select){
           statementManager =  new SelectManager((Select) statement, table);
        }
        else if (statement instanceof Update){
            statementManager =  new UpdateManager((Update) statement, table);
        }
        else if (statement instanceof Delete){
            statementManager =  new DeleteManager((Delete) statement, table);
        }
        return statementManager;
    }
}
