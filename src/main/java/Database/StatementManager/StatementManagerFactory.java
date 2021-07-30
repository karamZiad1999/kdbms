package Database.StatementManager;

import Database.Table.Table;
import SQL.Authorization;
import SQL.Statement.*;

public class StatementManagerFactory {
    public static StatementManager makeStatementManager(Statement statement, Table table, Authorization authorization){
        StatementManager statementManager = null;
        if(statement instanceof Create){
            if(authorization == Authorization.DATABASE_MAINTAINER) statementManager = new CreateManager((Create) statement, table);
        }
        else if (statement instanceof Insert){
            if(authorization.compareTo(Authorization.READ_AND_WRITE) >= 0) statementManager =  new InsertManager((Insert) statement, table);
        }
        else if (statement instanceof Select){
            if(authorization.compareTo(Authorization.READ_ONLY) >= 0) statementManager =  new SelectManager((Select) statement, table);
        }
        else if (statement instanceof Update){
            if(authorization.compareTo(Authorization.READ_AND_WRITE) >= 0) statementManager =  new UpdateManager((Update) statement, table);
        }
        else if (statement instanceof Delete){
            if(authorization.compareTo(Authorization.DATABASE_MAINTAINER) >= 0) statementManager =  new DeleteManager((Delete) statement, table);
        }
        return statementManager;
    }
}
