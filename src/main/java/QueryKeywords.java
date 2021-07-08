public class QueryKeywords {

    private String [] queryKeywords;
    private int keywordIndex;
    private int size;
    public QueryKeywords(String query){
        query = query.replace("\n", "");
        queryKeywords = query.split("[\\s,]");
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
