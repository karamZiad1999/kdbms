package database;

public class InsertLock {
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
