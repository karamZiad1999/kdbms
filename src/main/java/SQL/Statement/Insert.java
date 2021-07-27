package SQL.Statement;

import java.io.PrintWriter;

public class Insert implements InsertStatement {

        private PrintWriter out;
        private String tableName;
        private StatementKeywords statementKeywords;
        private StringBuilder recordBlock;
        private String primaryKey = null;

        public Insert(StatementKeywords statementKeywords){
                this.statementKeywords = statementKeywords;
                recordBlock = new StringBuilder();
                if(statementKeywords.hasNext())
                        if(statementKeywords.getNextKeyword().equalsIgnoreCase("into")) parseTableName();
        }

        private void parseTableName(){
                if(statementKeywords.hasNext()) tableName = statementKeywords.getNextKeyword();

                if(statementKeywords.hasNext())
                        if(statementKeywords.getNextKeyword().equals("(")) parsePrimaryKey();
        }

        private void parsePrimaryKey(){
                if(statementKeywords.hasNext()) primaryKey = statementKeywords.getNextKeyword();
                if(statementKeywords.hasNext()) parseFieldData();
        }

        private void parseFieldData(){
                if(statementKeywords.checkNext().equals(")") ) return;
                else if(statementKeywords.hasNext()) {
                        recordBlock.append( statementKeywords.getNextKeyword() + "\n");
                }

                if(statementKeywords.hasNext()) parseFieldData();
        }

        public String getRecordBlock()
        {
                String block = primaryKey + "\n" + recordBlock.toString();
                return block;
        }

        public String getPrimaryKey(){
                return primaryKey;
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
}
