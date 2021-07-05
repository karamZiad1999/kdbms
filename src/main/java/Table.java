import jdk.nashorn.internal.ir.BlockLexicalContext;

import java.util.HashMap;

public class Table {

    private Cache cache;
    private RecordFactory recordFactory;
    private FileManager fileManager;
    private HashMap<Integer, BlockInformation> recordIndex;


    public Table(String tableSrc, String metaDataSrc){

        fileManager = new FileManager(tableSrc);
        recordFactory = new RecordFactory(metaDataSrc);

    }

    public Record getRecord(int primaryKey){

        Record record = cache.getRecord(primaryKey);

        if(record == null){
            addRecord(primaryKey);
            record = cache.getRecord(primaryKey);
        }

        return record;

    }


    public void addRecord(int primaryKey){

        BlockInformation blockInformation = recordIndex.get(primaryKey);
        cache.addRecord(new Record(recordFactory.copyHashMapTemplate()), fileManager.getBlock(blockInformation.getByteOffset(), blockInformation.getBlockSize()));

    }

}
