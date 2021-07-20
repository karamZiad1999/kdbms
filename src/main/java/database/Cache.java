package database;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cache {


    private LinkedHashMap<String, Record> records;
    private final long MAX = 10000;

    Cache(){

        records = new LinkedHashMap<String, Record>() {
            protected boolean removeEldestEntry(Map.Entry<String, Record> eldest)
            {
                return size() > MAX;
            }
        };
    }

    public void addRecord(Record record, byte[] block){

         record.addRecord(block);
         records.put( record.getPrimaryKey(), record);

    }

    public Record getRecord(String primaryKey){

        Record record = records.get(primaryKey);
        return record;
    }
}
