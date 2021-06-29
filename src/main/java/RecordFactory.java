import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class RecordFactory {
    private HashMap<String, Field> fields;
    RandomAccessFile metaDataSrc;
    public RecordFactory(RandomAccessFile metaDataSrc){
        this.metaDataSrc = metaDataSrc;
        initializeFields();
    }

    private void initializeFields(){
        String line;
        String [] fieldMetaData;

        try{
            while((line = metaDataSrc.readLine())!= null){
                fieldMetaData = line.split(":");

                switch(fieldMetaData[1]){
                    case "Integer":
                        fields.put(fieldMetaData[0], new Field<Integer>("Integer"));
                        break;

                    case "String":
                        fields.put(fieldMetaData[0], new Field<String>("String"));
                        break;

                    case "Long":
                        fields.put(fieldMetaData[0], new Field<Long>("Long"));
                        break;

                    case "Boolean":
                        fields.put(fieldMetaData[0], new Field<Boolean>("Boolean"));
                        break;

                    case "Float":
                        fields.put(fieldMetaData[0], new Field<Float>("Float"));
                        break;

                    case "Double":
                        fields.put(fieldMetaData[0], new Field<Double>("Double"));
                        break;

                    default:
                        System.out.println("ERROR: field type invalid ");
                        break;
                }
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public HashMap<String, Field> copyHashMapTemplate(){

        HashMap<String, Field> clone = new HashMap<String, Field>();

        for(Map.Entry<String, Field> entry : fields.entrySet()){

            clone.put(entry.getKey(), new Field(entry.getValue().getType(), entry.getValue().getElement()));
        }

        return clone;
    }
}
