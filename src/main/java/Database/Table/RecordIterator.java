package Database.Table;

import Database.Table.Record.Record;

public interface RecordIterator {
    public boolean hasNext();
    public Record getNextRecord();
    public void deleteRecord();
}
