package database;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Map;

public class RecordFactory {

    private LinkedHashMap<String, Field> fields;
    RandomAccessFile metaDataSrc;
    String primaryKey;

    public RecordFactory(String tableName){
        try {
            this.metaDataSrc = new RandomAccessFile(tableName + "-metaData.kdb", "rw");
        }catch(Exception e){
            System.out.println(e);
        }
        fields = new LinkedHashMap<String,Field>();
        initializeFields();
    }

    private void initializeFields(){
        String line;
        String [] fieldMetaData;

        try{
            int i = 0;

            if((line = metaDataSrc.readLine()) != null) {

                fieldMetaData = line.split(":");
                fields.put(fieldMetaData[0], new Field(fieldMetaData[1]));
                primaryKey = fieldMetaData[0];
            }
            while((line = metaDataSrc.readLine())!= null){
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
            clone.put(entry.getKey(), new Field(entry.getValue().getType(), entry.getValue().getValue()));
        }
        return clone;
    }

    public String getPrimaryKey(){return primaryKey;}
}
