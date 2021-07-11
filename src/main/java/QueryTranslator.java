import java.util.HashSet;

public class QueryTranslator {
    QueryHandler queryHandler;
    QueryKeywords queryKeywords;
    DB database;

    public QueryTranslator(){
        database = DB.getInstance();
        queryHandler = new QueryHandler();
    }

    public void translateQuery(String query){
        queryKeywords = new QueryKeywords(query);
        String keywords;
        if(queryKeywords.hasNext())
        {
            switch(queryKeywords.getNextKeyword().toLowerCase()) {
                case "create":
                    Create createAction = new Create(queryKeywords);
                    queryHandler.handleAction(createAction);
                    break;

                case "insert":
                    Insert insertAction = new Insert(queryKeywords);
                    queryHandler.handleAction(insertAction);
                    break;

                default:
                    return;

                case "delete":
                    Delete deleteAction = new Delete(queryKeywords);
                    queryHandler.handleAction(deleteAction);
                    break;

//                case "select":
//                    Create createAction = new Create();
//                    break;

//            }
            }


        }
    }




}
