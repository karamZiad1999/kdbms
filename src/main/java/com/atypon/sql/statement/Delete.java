package com.atypon.sql.statement;

import com.atypon.sql.Token;
import com.atypon.sql.Tokenizer;

import java.io.PrintWriter;

public class Delete implements DeleteStatement{

    private PrintWriter outputStream;
    private String tableName;
    private Tokenizer tokenizer;
    private String field;
    private String condition ;
    private String value;
    private String schema; 

    public Delete(Tokenizer tokenizer, String schema){
        this.tokenizer = tokenizer;
        this.schema = schema; 
        parseTableName();
    }

    protected void parseTableName(){
        eat("FROM");
        tableName = eat("IDENTIFIER");
        parseCondition();
    }

    private void parseCondition(){
        eat("WHERE");
        field = eat("IDENTIFIER");
        condition = eat("CONDITION");
        value = eat("IDENTIFIER");
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

    private String eat(String tokenType){
        Token token = tokenizer.getNextToken();
        if(token==null||!token.getType().equals(tokenType)){
            System.out.println("Error: Expected " + tokenType + " instead of " + token.getType());
            throw new IllegalArgumentException();
        }
        else return token.getValue();
    }


    @Override
    public PrintWriter getOutputStream() {
        return outputStream;
    }

    @Override
    public void setOutputStream(PrintWriter outputStream) {
        this.outputStream = outputStream;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSchemaName(){
        return schema;
    }
}
