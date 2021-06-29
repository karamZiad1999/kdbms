import java.util.HashMap;

public class Record {

    TableMetaData metaData;
    HashMap<String, Field> fields;



    public Record( HashMap<String, Field> fields){
        this.fields = fields;
    }

    public Field getField(String name)
    {
        return fields.get(name);

    }

}
