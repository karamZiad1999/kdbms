import java.util.HashMap;
import java.util.Map;

public class Record {

    private HashMap<String, Field> fields;

    public Record(HashMap<String, Field> fields){ this.fields = fields;}

    public Field getField(String name){ return fields.get(name); }

    public void addRecord(byte[] b) {

        StringBuilder recordLine = new StringBuilder();
        String type;
        String stringFieldValue;

        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            for (int i = 0; i < b.length; i++) {

                if (b[i] == '\n') {
                    stringFieldValue = recordLine.toString();
                    type = entry.getValue().getType();

                    switch (type) {
                        case "Integer":
                            entry.getValue().set(Integer.parseInt(stringFieldValue));
                            break;

                        case "String":
                            break;

                        case "Long":
                            entry.getValue().set(Long.parseLong(stringFieldValue));
                            break;

                        case "Boolean":
                            entry.getValue().set(Boolean.parseBoolean(stringFieldValue));
                            break;

                        case "Float":
                            entry.getValue().set(Float.parseFloat(stringFieldValue));
                            break;

                        case "Double":
                            entry.getValue().set(Double.parseDouble(stringFieldValue));
                            break;

                        default:
                            System.out.println("ERROR: field type invalid ");
                            break;
                    }
                } else recordLine.append(b[i]);

            }
        }
    }


}
