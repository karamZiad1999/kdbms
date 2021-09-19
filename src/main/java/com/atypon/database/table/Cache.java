package com.atypon.database.table;

import com.atypon.database.table.Record.Record;
import com.atypon.database.table.Record.RecordFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cache {


    private LinkedHashMap<String, Record> records;
    private final long MAX = 10000;
    private TableManager tableManager;
    private RecordFactory recordFactory;
    Cache(TableManager tableManager, RecordFactory recordFactory){
        this.tableManager = tableManager;
        this.recordFactory=recordFactory;
        records = new LinkedHashMap<String, Record>(){
            protected boolean removeEldestEntry(Map.Entry<String, Record> eldest)
            {
                return size() > MAX;
            }
        };
    }

    public void addRecord(Record record){
        String primaryKey = record.getPrimaryKey();
         records.put(primaryKey, record);
    }

    public Record getRecord(String primaryKey){
        Record record = records.get(primaryKey);
        if(record==null&&tableManager.getRecordInfo(primaryKey)!=null) {
            record = recordFactory.createRecord(tableManager.getBlock(primaryKey));
            addRecord(record);
        }
        return record;
    }
}
