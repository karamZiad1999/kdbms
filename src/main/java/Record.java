import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Record {

    private LinkedHashMap<String, Field> fields;

    public Record(LinkedHashMap<String, Field> fields){ this.fields = fields;}

    public Field getField(String name){ return fields.get(name); }

    public void addRecord(byte[] b) {


        String [] fieldValuesInString;
        String blockInString = new String(b, StandardCharsets.UTF_8);
        fieldValuesInString = blockInString.split("\n");
        for (Map.Entry<String, Field> entry : fields.entrySet()) {

        }
    }

    public boolean checkCondition(String field, String condition, String value){
        return getField(field).checkCondition(condition, value);
    }


}
