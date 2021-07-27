package SQL.Statement;

import java.io.PrintWriter;

public class Delete implements DeleteStatement, SelectStatement {

    private PrintWriter out;
    private String tableName;
    private StatementKeywords statementKeywords;
    private String field;
    private String condition ;
    private String value;
    boolean isCondition = true;

    public Delete(StatementKeywords statementKeywords){

        this.statementKeywords = statementKeywords;
        if(statementKeywords.hasNext())
            if(statementKeywords.getNextKeyword().equalsIgnoreCase("from")) parseTableName();

    }

    protected void parseTableName(){
        if(statementKeywords.hasNext()) tableName = statementKeywords.getNextKeyword();

        if(statementKeywords.hasNext())
            if(statementKeywords.getNextKeyword().equalsIgnoreCase("where")){parseField();}


    }

    private void parseField(){
        if(statementKeywords.hasNext()) field = statementKeywords.getNextKeyword();
        if(statementKeywords.hasNext()) parseCondition();
    }


    private void parseCondition(){

        condition = statementKeywords.getNextKeyword();
        if(statementKeywords.hasNext()) parseValue();

    }

    public void parseValue(){
        value = statementKeywords.getNextKeyword();
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

    public String getTableName(){
        return tableName;
    }


    public void setOutputStream(PrintWriter out) {
        this.out = out;
    }

    public PrintWriter getOutputStream() {
        return out;
    }

    public boolean isCondition() {
        return isCondition;
    }
}
