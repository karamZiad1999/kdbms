package com.atypon.SQL.Statement;

public class StatementFactory {
    public static Statement makeStatement(String query){
        StatementKeywords statementKeywords = new StatementKeywords(query);
        String keywords;

        if(statementKeywords.hasNext())
        {
            switch(statementKeywords.getNextKeyword().toLowerCase()) {
                case "create":
                    return new Create(statementKeywords);

                case "insert":
                    return new Insert(statementKeywords);

                case "delete":
                    return new Delete(statementKeywords);

                case "update":
                    return new Update(statementKeywords);

                case "select":
                    return new Select(statementKeywords);

                default:
                    return null;
            }
        }
        return null;
    }
}
