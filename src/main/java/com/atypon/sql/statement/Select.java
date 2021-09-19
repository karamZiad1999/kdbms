package com.atypon.sql.statement;

import com.atypon.sql.Token;
import com.atypon.sql.Tokenizer;

import java.io.PrintWriter;

public class Select implements SelectStatement{

    private Tokenizer tokenizer;
    private String field;
    private String condition;
    private String value;
    private boolean isCondition=false;
    private PrintWriter outputStream;
    private String tableName;
    private String schema;
    private boolean isMeta = false;

    public Select(Tokenizer tokenizer, String schema){
        this.tokenizer = tokenizer;
        this.schema = schema;
        parse();
    }

    private void parse(){
        if(isNextToken("*")) eat("*");
        if(isNextToken("META")) {
            eat("META");
            isMeta=true;
        }
        eat("FROM");
        parseTableName();
    }

    private void parseTableName(){
        tableName = eat("IDENTIFIER");
        if(isNextToken("WHERE")) {
            isCondition=true;
            eat("WHERE");
            parseCondition();
        }
    }

    private void parseCondition(){
        field = eat("IDENTIFIER");
        condition = eat("CONDITION");
        value = eat("IDENTIFIER");
    }

    public boolean isCondition(){
        return isCondition;
    }

    public String getTableName(){
        return tableName;
    }

    @Override
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String getCondition() {
        return condition;
    }


    @Override
    public String getValue() {
        return value;
    }

    @Override
    public PrintWriter getOutputStream() {
        return outputStream;
    }

    @Override
    public void setOutputStream(PrintWriter outputStream) {
        this.outputStream = outputStream;
    }

    private String eat(String tokenType){
        Token token = tokenizer.getNextToken();
        if(token==null||!token.getType().equals(tokenType)){
            System.out.println("Error: Expected " + tokenType + " instead of " + token.getType());
            throw new IllegalArgumentException();
        }
        else return token.getValue();
    }

    public String getSchemaName(){
        return schema;
    }

    private boolean isNextToken(String tokenType){
        return tokenizer.isNextToken(tokenType);
    }

    public boolean isMeta(){
        return isMeta;
    }
}
