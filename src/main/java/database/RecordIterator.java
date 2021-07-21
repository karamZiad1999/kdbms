package database;

public interface RecordIterator {
    public boolean hasNext();
    public Record getNextRecord();
    public void deleteRecord();
}
