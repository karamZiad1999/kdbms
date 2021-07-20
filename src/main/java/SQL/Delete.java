package SQL;

public class Delete {


    private String tableName;
    private QueryKeywords queryKeywords;
    private String field;
    private String condition;
    private String value;

    public Delete(QueryKeywords queryKeywords){

        this.queryKeywords = queryKeywords;
        if(queryKeywords.hasNext())
            if(queryKeywords.getNextKeyword().equalsIgnoreCase("from")) parseTableName();

    }

    private void parseTableName(){

        if(queryKeywords.hasNext()) tableName = queryKeywords.getNextKeyword();

        if(queryKeywords.hasNext())
            if(queryKeywords.getNextKeyword().equalsIgnoreCase("where")) parseField();
    }

    private void parseField(){

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
}
