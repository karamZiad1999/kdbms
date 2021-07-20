package SQL;

import java.util.HashMap;

public class Update {

    private String tableName;
    private QueryKeywords queryKeywords;
    private HashMap<String, String> updates;
    private String field;
    private String condition;
    private String value;

    public Update(QueryKeywords queryKeywords){

        this.queryKeywords = queryKeywords;
        updates = new HashMap<String, String>();
        if(queryKeywords.hasNext()) parseTableName();

    }

    private void parseTableName(){

       tableName = queryKeywords.getNextKeyword();

        if(queryKeywords.hasNext())
            if(queryKeywords.getNextKeyword().equalsIgnoreCase("set")) parseUpdates();
    }

    private void parseUpdates(){

        String field = null;
        String value = null;
        if(queryKeywords.checkNext().equalsIgnoreCase("where")) parseField();

        if(queryKeywords.hasNext()) field = queryKeywords.getNextKeyword();
        if(queryKeywords.hasNext())
            if(queryKeywords.getNextKeyword().equals("="))
                value = queryKeywords.getNextKeyword();

        if((field != null)&& (value != null) ) updates.put(field, value);
        if(queryKeywords.hasNext()) parseUpdates();

    }


    private void parseField(){
        queryKeywords.getNextKeyword();
        if(queryKeywords.hasNext()) field = queryKeywords.getNextKeyword();
        if(queryKeywords.hasNext()) parseCondition();

    }


    private void parseCondition(){

        condition = queryKeywords.getNextKeyword();
        if(queryKeywords.hasNext()) parseValue();

    }

    public void parseValue(){
        value = queryKeywords.getNextKeyword();
    }
    public String getTableName(){
        return tableName;
    }


    public String getField() {
        return field;
    }
    public String getCondition(){
        return condition;
    }

    public String getValue(){
        return value;
    }

    public HashMap<String, String> getUpdates(){
        return updates;
    }
}
