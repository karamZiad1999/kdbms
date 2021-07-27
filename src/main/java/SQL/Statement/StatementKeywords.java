package SQL.Statement;

public class StatementKeywords {

    private String [] queryKeywords;
    private int keywordIndex;
    private int size;
    public StatementKeywords(String query){
        query = query.trim().replace(",", " ");
        queryKeywords = query.split("\\s+");
        keywordIndex = 0;
        size = queryKeywords.length;
    }

    public String getNextKeyword(){
        return queryKeywords[keywordIndex++];
    }

    public boolean hasNext(){
        if(keywordIndex < size) return true;
        return false;
    }

    public String checkNext(){
        return queryKeywords[keywordIndex];
    }
}
