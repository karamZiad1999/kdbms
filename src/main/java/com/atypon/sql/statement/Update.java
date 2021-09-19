package com.atypon.sql.statement;

import com.atypon.sql.Token;
import com.atypon.sql.Tokenizer;

import java.io.PrintWriter;
import java.util.HashMap;

public class Update implements UpdateStatement{

    private PrintWriter outputStream;
    private String tableName;
    private Tokenizer tokenizer;
    private HashMap<String, String> updates;
    private String field;
    private String condition;
    private String value;
    private boolean isCondition = false;
    private String schema; 

    public Update(Tokenizer tokenizer, String schema){
        this.tokenizer = tokenizer;
        updates = new HashMap<String, String>();
        this.schema = schema;
        parse();
    }

    private void parse(){
        parseTableName();
    }
    private void parseTableName(){
       tableName = eat("IDENTIFIER");
       parseUpdate();
    }

    private void parseUpdate(){
        eat("SET");
        String fieldName = eat("IDENTIFIER");
        eat("CONDITION");
        String newValue = eat("IDENTIFIER");
        updates.put(fieldName, newValue);
        if(isNextToken("IDENTIFIER"))parseUpdate();
        else parseCondition();
    }

    private void parseCondition(){
        eat("WHERE");
        isCondition = true;
        field = eat("IDENTIFIER");
        condition = eat("CONDITION");
        value = eat("IDENTIFIER");
    }

    @Override
    public PrintWriter getOutputStream() {
        return outputStream;
    }

    @Override
    public void setOutputStream(PrintWriter outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public HashMap<String, String> getUpdates() {
        return updates;
    }

    @Override
    public String getField() {
        return field;
    }
    @Override
    public String getCondition() {
        return condition;
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isCondition() {
        return isCondition;
    }

    public String getSchemaName(){
        return schema;
    }

    private String eat(String tokenType){
        Token token = tokenizer.getNextToken();
        if(token==null||!token.getType().equals(tokenType)){
            System.out.println("Error: Expected " + tokenType + " instead of " + token.getType());
            throw new IllegalArgumentException();
        }
        else return token.getValue();
    }

    private boolean isNextToken(String tokenType){
        return tokenizer.isNextToken(tokenType);
    }
}
