package SQL;

import database.KDBMS;

import java.io.PrintWriter;

public class QueryTranslator {
    QueryHandler queryHandler;
    QueryKeywords queryKeywords;
    KDBMS database;
    PrintWriter out;

    public QueryTranslator(PrintWriter out){
        database = KDBMS.getInstance();
        queryHandler = new QueryHandler();
        this.out = out;
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

                case "update":
                    Update updateAction = new Update(queryKeywords);
                    queryHandler.handleAction(updateAction);
                    break;

                case "select":
                    Select selectAction = new Select(queryKeywords);
                    queryHandler.handleAction(selectAction, out);
                    break;

            }
            }


        }
    }

