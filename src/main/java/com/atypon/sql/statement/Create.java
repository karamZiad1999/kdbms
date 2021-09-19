package com.atypon.sql.statement;

import com.atypon.sql.Token;
import com.atypon.sql.Tokenizer;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Create implements CreateStatement {

    private String tableName;
    private LinkedHashMap<String, String> fieldMetaData;
    private String primaryKey;
    private Tokenizer tokenizer;
    private HashMap<String,String> foreignKeys;
    private PrintWriter outputStream;
    private boolean isCreateSchema = false;
    private String schemaName;


    public Create(Tokenizer tokenizer){
        this.tokenizer = tokenizer;
        parseSchemaName();
    }

    public Create(Tokenizer tokenizer, String schema){
        foreignKeys = new HashMap<String, String>();
        fieldMetaData = new LinkedHashMap<String, String>();
        this.tokenizer = tokenizer;
        this.schemaName=schema;
        parseTableName();
    }

    private void parseSchemaName() {
        eat("SCHEMA");
        schemaName = eat("IDENTIFIER");
        isCreateSchema = true;
    }

    private void parseTableName(){
        eat("TABLE");
       tableName = eat("IDENTIFIER");
       parseFieldMetaData();
    }

    private void parseFieldMetaData(){
        String fieldName = eat("IDENTIFIER");
        String dataType = eat("DATATYPE");
        fieldMetaData.put(fieldName, dataType);
        if(isNextToken("IDENTIFIER")) parseFieldMetaData();
        else if (isNextToken("PRIMARY")) parsePrimaryKey();
        else if (isNextToken("FOREIGN")) parseForeignKey();
    }

    private void parsePrimaryKey(){
        eat("PRIMARY");
        eat("KEY");
        primaryKey = eat("IDENTIFIER");
        if(isNextToken("FOREIGN")) parseForeignKey();
    }

    private void parseForeignKey(){
        eat("FOREIGN");
        eat("KEY");
        String field = eat("IDENTIFIER");
        eat("REFERENCES");
        String relation = eat("IDENTIFIER");
        String foreignField = eat("IDENTIFIER");
        String mapEntry = relation + ":" + foreignField;
        foreignKeys.put(field, mapEntry);
        if(isNextToken("FOREIGN")){
            if (isNextToken("PRIMARY")) parsePrimaryKey();
            else if (isNextToken("FOREIGN")) parseForeignKey();
        }
    }


    public String getMetaDataInString(){
        StringBuilder metaData = new StringBuilder();

        for(Map.Entry<String,String> entry: fieldMetaData.entrySet() ){
            String fieldName = entry.getKey();
            String dataType = entry.getValue();
            metaData.append(fieldName);
            metaData.append(":");
            metaData.append(dataType);
            if(fieldName.equals(primaryKey)) metaData.append(":primary");
            else if(dataType.equals("relation")) metaData.append(":" + foreignKeys.get(fieldName));
            metaData.append("\n");
        }
        return metaData.toString();
    }

    private String eat(String tokenType){
        Token token = tokenizer.getNextToken();
        if(token==null||!token.getType().equals(tokenType)){
            System.out.println("Error: Expected " + tokenType + " instead of " + token.getType());
            throw new IllegalArgumentException();
        }
        return token.getValue();
    }

    private boolean isNextToken(String tokenType){
        return tokenizer.isNextToken(tokenType);
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public boolean isCreateSchema() {
        return isCreateSchema;
    }

    public String getSchemaName() {
        return schemaName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    public LinkedHashMap<String, String> getFieldMetaData() {
        return fieldMetaData;
    }

    @Override
    public String getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public PrintWriter getOutputStream() {
        return outputStream;
    }

    @Override
    public void setOutputStream(PrintWriter outputStream) {
        this.outputStream = outputStream;
    }
}
