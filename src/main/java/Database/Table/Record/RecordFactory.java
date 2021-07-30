package Database.Table.Record;

import Database.Table.Record.Field.Field;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Map;

public class RecordFactory {

    private LinkedHashMap<String, Field> fields;
    File metaDataSrc;
    String primaryKey;

    public RecordFactory(String tableName){
        metaDataSrc = new File(tableName + "-metaData.kdb");
        fields = new LinkedHashMap<String,Field>();
        initializeFields();
    }

    private void initializeFields(){
        String line;
        String [] fieldMetaData;

        try(RandomAccessFile metaDataSrcReader = new RandomAccessFile(metaDataSrc, "rw")){
            int i = 0;

            if ((line = metaDataSrcReader.readLine()) != null){                 //reads primary key
                fieldMetaData = line.split(":");
                fields.put(fieldMetaData[0], new Field(fieldMetaData[1]));
                primaryKey = fieldMetaData[0];
            }
            while((line = metaDataSrcReader.readLine())!= null){
                fieldMetaData = line.split(":");
                fields.put(fieldMetaData[0], new Field(fieldMetaData[1]));
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public LinkedHashMap<String, Field> copyHashMapTemplate(){
        LinkedHashMap<String, Field> clone = new LinkedHashMap<String, Field>();

        for(Map.Entry<String, Field> entry : fields.entrySet()){
            clone.put(entry.getKey(), new Field(entry.getValue().getType()));
        }
        return clone;
    }

    public String getPrimaryKey(){return primaryKey;}
}
