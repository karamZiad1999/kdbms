import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Insert {

        private String tableName;
        private QueryKeywords queryKeywords;
        private StringBuilder recordBlock;
        private String primaryKey = null;

        public Insert(QueryKeywords queryKeywords){
                this.queryKeywords = queryKeywords;
                recordBlock = new StringBuilder();
                if(queryKeywords.hasNext())
                        if(queryKeywords.getNextKeyword().equalsIgnoreCase("into")) parseTableName();
        }

        private void parseTableName(){

                if(queryKeywords.hasNext()) tableName = queryKeywords.getNextKeyword();

                if(queryKeywords.hasNext())
                        if(queryKeywords.getNextKeyword().equals("(")) parsePrimaryKey();
        }

        private void parsePrimaryKey(){
                if(queryKeywords.hasNext()) primaryKey = queryKeywords.getNextKeyword();
                if(queryKeywords.hasNext()) parseFieldData();
        }


        private void parseFieldData(){


                if(queryKeywords.checkNext().equals(")") ) return;
                else if(queryKeywords.hasNext()) {
                        recordBlock.append( queryKeywords.getNextKeyword() + "\n");
                }

                if(queryKeywords.hasNext()) parseFieldData();

        }

        public String getTableName(){
                return tableName;
        }

        public String getRecordBlock()
        {
                String block = primaryKey + "\n" + recordBlock.toString();
                return block;
        }

        public String getPrimaryKey(){
                return primaryKey;
        }
}
