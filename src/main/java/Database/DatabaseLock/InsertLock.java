package Database.DatabaseLock;

import Database.Table.Table;

public class InsertLock extends DatabaseLock {
    Table table;

    public InsertLock(Table table){
        this.table = table;
    }

    public void lock(){
        table.lockTableWrite();
    }

    public void unlock(){
        table.unlockTableWrite();
    }
}
