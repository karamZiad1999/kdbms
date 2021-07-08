import java.util.HashMap;
import java.util.Map;

public class Create implements Action{

    private String tableName;
    private HashMap<String, String> fieldMetaData;
    private QueryKeywords queryKeywords;
    private String field;
    private String type;



    public Create(QueryKeywords queryKeywords){

        fieldMetaData = new HashMap<String, String>();
        this.queryKeywords = queryKeywords;

        if(queryKeywords.hasNext())
            if(queryKeywords.getNextKeyword().toLowerCase().equals("table")) parseTableName();
    }

    private void parseTableName(){

        if(queryKeywords.hasNext()) tableName = queryKeywords.getNextKeyword();

        if(queryKeywords.hasNext())
            if(queryKeywords.getNextKeyword().equals("(")) parseFieldMetaData();
    }

    private void parseFieldMetaData(){

        if(queryKeywords.checkNext().equals(")") ) return;

        if(queryKeywords.hasNext()){
            field = queryKeywords.getNextKeyword();

            if(queryKeywords.hasNext()){
                type = queryKeywords.getNextKeyword();
                fieldMetaData.put(field, type);

            }
        }

        if(queryKeywords.hasNext()) parseFieldMetaData();

    }

    public String getTableName(){
        return tableName;
    }

    public String getMetaDataInString(){

        StringBuilder metaData = new StringBuilder();

        for(Map.Entry entry: fieldMetaData.entrySet() ){

            metaData.append(entry.getKey());
            metaData.append(":");
            metaData.append(entry.getValue());
            metaData.append("\n");

        }

        return metaData.toString();
    }

}
