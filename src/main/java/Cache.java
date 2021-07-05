import java.util.HashMap;
import java.util.Map;

public class Cache {

    private HashMap<Integer, Record> records;

    Cache(){ records = new HashMap<Integer, Record>();}

    public void addRecord(Record record, byte[] block){
         record.addRecord(block);
         records.put((Integer) record.getField("primary key").getElement(), record);
    }

    public Record getRecord(int primaryKey){
        Record record = records.get(primaryKey);
        return record;
    }
}
