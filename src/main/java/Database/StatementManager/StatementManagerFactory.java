package Database.StatementManager;

import Database.Table.Table;
import SQL.Statement.*;

public class StatementManagerFactory {
    public static StatementManager makeStatementManager(Statement statement, Table table) {
        if(statement instanceof Create){
            return new CreateManager((Create) statement, table);
        }
        else if (statement instanceof Insert){
            return new InsertManager((Insert) statement, table);
        }
        else if (statement instanceof Select){
            return new SelectManager((Select) statement, table);
        }
        else if (statement instanceof Update){
            return new UpdateManager((Update) statement, table);
        }
        else if (statement instanceof Delete){
            return new DeleteManager((Delete) statement, table);
        }
        else return null;
    }
}
