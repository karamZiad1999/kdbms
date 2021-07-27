package SQL.Statement;

import java.io.PrintWriter;
import java.util.HashMap;

public class Update implements UpdateStatement, SelectStatement {

    PrintWriter out;
    private String tableName;
    private StatementKeywords statementKeywords;
    private HashMap<String, String> updates;
    private String field;
    private String condition;
    private String value;
    private boolean isCondition = true;
    public Update(StatementKeywords statementKeywords){
        this.statementKeywords = statementKeywords;

        updates = new HashMap<String, String>();
        if(statementKeywords.hasNext()) parseTableName();
    }

    private void parseTableName(){
       tableName = statementKeywords.getNextKeyword();

        if(statementKeywords.hasNext())
            if(statementKeywords.getNextKeyword().equalsIgnoreCase("set")) parseUpdates();
    }

    private void parseUpdates(){
        String field = null;
        String value = null;
        if(statementKeywords.checkNext().equalsIgnoreCase("where")) parseField();

        if(statementKeywords.hasNext()) field = statementKeywords.getNextKeyword();
        if(statementKeywords.hasNext())
            if(statementKeywords.getNextKeyword().equals("="))
                value = statementKeywords.getNextKeyword();

        if((field != null)&& (value != null) ) updates.put(field, value);
        if(statementKeywords.hasNext()) parseUpdates();
    }


    private void parseField(){
        statementKeywords.getNextKeyword();
        if(statementKeywords.hasNext()) field = statementKeywords.getNextKeyword();
        if(statementKeywords.hasNext()) parseCondition();
    }

    private void parseCondition(){
        condition = statementKeywords.getNextKeyword();
        if(statementKeywords.hasNext()) parseValue();
    }

    public void parseValue(){value = statementKeywords.getNextKeyword();}

    public String getTableName(){return tableName;}


    public void setOutputStream(PrintWriter out) {this.out = out;}

    public PrintWriter getOutputStream() {
       return out;
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

    public boolean isCondition() {
        return isCondition;
    }
}
