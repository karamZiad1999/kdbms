package SQL.Statement;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class Create implements CreateStatement {

    private PrintWriter out;
    private String tableName;
    private LinkedHashMap<String, String> fieldMetaData;
    private StatementKeywords statementKeywords;
    private String field;
    private String type;
    private String primaryKey;

    public Create(StatementKeywords statementKeywords){
        fieldMetaData = new LinkedHashMap<String, String>();
        this.statementKeywords = statementKeywords;

        if(statementKeywords.hasNext())
            if(statementKeywords.getNextKeyword().toLowerCase().equals("table")) parseTableName();
    }

    private void parseTableName(){
        if(statementKeywords.hasNext()) tableName = statementKeywords.getNextKeyword();

        if(statementKeywords.hasNext())
            if(statementKeywords.getNextKeyword().equals("(")) parseFieldMetaData();
    }

    private void parseFieldMetaData(){
        if(statementKeywords.checkNext().equals(")") ) return;

        if(statementKeywords.hasNext()){
            field = statementKeywords.getNextKeyword();

            if(statementKeywords.hasNext()){
                type = statementKeywords.getNextKeyword();
                fieldMetaData.put(field, type);
            }
        }
        if(statementKeywords.hasNext()) parseFieldMetaData();
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

    public String getTableName(){return tableName;}

    public void setOutputStream(PrintWriter out) {this.out = out;}

    public PrintWriter getOutputStream() {return out;}

    public String getPrimaryKey(){return primaryKey;}

}
