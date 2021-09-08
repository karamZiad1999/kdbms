package com.atypon.database.table;

import com.atypon.database.table.Record.Record;

public interface RecordIterator {
    public boolean hasNext();
    public Record getNextRecord();
    public void deleteRecord();
}
