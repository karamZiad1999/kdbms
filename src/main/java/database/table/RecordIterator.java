package database.table;

import database.table.Record.Record;

public interface RecordIterator {
    public boolean hasNext();
    public Record getNextRecord();
    public void deleteRecord();
}
