package com.atypon.sql.statement;

import com.atypon.sql.Token;
import com.atypon.sql.Tokenizer;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Insert implements InsertStatement {

        private PrintWriter out;
        private String tableName;
        private Tokenizer tokenizer;
        private StringBuilder recordBlock;
        private LinkedHashMap<String, String> valuesMap;
        private ArrayList<String> valueList;
        private boolean isValuesMapped = false;
        private PrintWriter outputStream;
        private String schema; 

        public Insert(Tokenizer tokenizer, String schema){
                this.tokenizer = tokenizer;
                recordBlock = new StringBuilder();
                this.schema = schema;
                parse();
        }

        private void parse(){
                eat("INTO");
                parseTableName();
        }

        private void parseTableName(){
               tableName = eat("IDENTIFIER");
//               if(isNextTokenTwo("IDENTIFIER", "CONDITION")){
//                       valuesMap = new LinkedHashMap<String, String>();
//                       parseMappedValue();
//                       isValuesMapped=true;
//               }
//               else{
                       valueList = new ArrayList<String>();
                       parseValue();
//               }
        }

        private void parseMappedValue(){
                String field = eat("IDENTIFIER");
                eat("CONDITION");
                String value = eat("IDENTIFIER");
                valuesMap.put(field, value);
                if(isNextToken("IDENTIFIER")) parseMappedValue();
        }

        private void parseValue(){
                String value = eat("IDENTIFIER");
                valueList.add(value);
                if(isNextToken("IDENTIFIER")) parseValue();
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

        private boolean isNextTokenTwo(String firstToken, String secondToken){
                return tokenizer.isNextTwoTokens(firstToken, secondToken);
        }

        public PrintWriter getOut() {
                return out;
        }

        public void setOut(PrintWriter out) {
                this.out = out;
        }

        @Override
        public String getTableName() {
                return tableName;
        }

        public void setTableName(String tableName) {
                this.tableName = tableName;
        }

        public LinkedHashMap<String, String> getValuesMap() {
                return valuesMap;
        }

        public ArrayList<String> getValueList() {
                return valueList;
        }

        public boolean isValuesMapped() {
                return isValuesMapped;
        }

        @Override
        public PrintWriter getOutputStream() {
                return outputStream;
        }

        @Override
        public void setOutputStream(PrintWriter outputStream) {
                this.outputStream = outputStream;
        }

        public String getSchemaName(){
                return schema;
        }
}
