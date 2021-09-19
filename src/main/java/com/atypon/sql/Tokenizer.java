package com.atypon.sql;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public class Tokenizer {

    LinkedHashMap<String, String> tokenTypes;
    private int cursor;
    private int tokenCount;
    private String[] tokens;

    public Tokenizer(String query){
        tokenTypes = new LinkedHashMap<String, String>();
        cursor = -1;
        initializeTokens();
        splitIntoTokens(query);
    }

    private void initializeTokens(){
        tokenTypes.put("use", "USE");
        tokenTypes.put("(create|insert|select|update|delete)", "STATEMENT");
        tokenTypes.put("schema", "SCHEMA");
        tokenTypes.put("table", "TABLE");
        tokenTypes.put("(string|integer|long|float|double|relation|boolean)", "DATATYPE");
        tokenTypes.put("where", "WHERE"); 
        tokenTypes.put("into", "INTO"); 
        tokenTypes.put("from", "FROM");
        tokenTypes.put("set", "SET");
        tokenTypes.put("references", "REFERENCES");
        tokenTypes.put("primary", "PRIMARY");
        tokenTypes.put("foreign", "FOREIGN");
        tokenTypes.put("meta", "META");
        tokenTypes.put("key", "KEY");
        tokenTypes.put("\\s*[=><(>=)(<=)(!=)]\\s*", "CONDITION" );
        tokenTypes.put("\\*", "*");
        tokenTypes.put(".+", "IDENTIFIER");

    }
    
    private  void splitIntoTokens(String query){
        tokens = query.trim().trim().split("([\\(\\)\\s+,]|((?<=[=<>])|(?=[=<>])))");
        tokenCount = tokens.length;
    }


    public Token getNextToken(){return getToken(++cursor);}

    public boolean isNextToken(String tokenType){
        Token nextToken = peekNextToken();
        if(nextToken==null) return false;
        return nextToken.getType().equals(tokenType);
    }

    private Token getToken(int index){
        Token token = null;
        if(index < tokenCount){
            token = new Token();
            String value = tokens[index];
            if(value.equals("")) return getNextToken();
            for(Map.Entry<String, String> tokenType : tokenTypes.entrySet()){
                String type = tokenType.getKey();
                if(value.toLowerCase().matches(type)){
                    String tokenValue = tokenType.getValue();
                    token.setValue(value);
                    token.setType(tokenValue);
                    break;
                }
            }
        }
        return token;
    }
    public Token peekNextToken(){
        Token token = getNextToken();
        --cursor;
        return token;
    }

    public String peekNext(){
        String next = peekNextToken().getValue();
        return next;
    }


    public boolean isNextTwoTokens(String firstType, String secondType){
        int first = cursor + 1;
        int second = cursor + 2;

        Token firstToken = getToken(first);
        Token secondToken = getToken(second);
        if(firstToken==null||secondToken==null) return false;
        return (firstToken.getType().equals(firstType)&&secondToken.getType().equals(secondType));
    }
}
