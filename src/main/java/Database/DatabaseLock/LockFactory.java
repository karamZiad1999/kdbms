package Database.DatabaseLock;

import Database.Table.Table;
import SQL.Statement.*;

public class LockFactory {
    public static DatabaseLock makeLock(Statement statement, Table table) {
        if(statement instanceof Create){
            return new CreateLock(table);
        }
        else if (statement instanceof Insert){
            return new InsertLock(table);
        }
        else if (statement instanceof Select){
            return new SelectLock(table);
        }
        else if (statement instanceof Update){
            return new UpdateLock(table);
        }
        else if (statement instanceof Delete){
            return new DeleteLock(table);
        }
        else return null;
    }
}
