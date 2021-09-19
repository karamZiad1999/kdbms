package com.atypon.sql.statement;

import com.atypon.sql.Token;
import com.atypon.sql.Tokenizer;

public class StatementFactory {

    public static Statement makeStatement(String query) {
        Tokenizer tokenizer = new Tokenizer(query);

        if(tokenizer.peekNext().equalsIgnoreCase("CREATE")){
            eat(tokenizer, "STATEMENT");
            return new Create(tokenizer);
        }

        eat(tokenizer, "USE");
        String schema = eat(tokenizer, "IDENTIFIER");
        String statement = eat(tokenizer,"STATEMENT");

        switch (statement.toUpperCase()){
            case "CREATE":
                return new Create(tokenizer, schema);
            case "INSERT":
                return new Insert(tokenizer, schema);

            case "DELETE":
                return new Delete(tokenizer, schema);

            case "UPDATE":
                return new Update(tokenizer, schema);

            case "SELECT":
                return new Select(tokenizer, schema);

            default:
                return null;
        }
    }

    private static String eat(Tokenizer tokenizer, String tokenType){
        Token token = tokenizer.getNextToken();
        if(token==null||!token.getType().equals(tokenType)) {
            System.out.println("Error: Expected " + tokenType + " instead of " + token.getType());
            throw new IllegalArgumentException();
        }
        else return token.getValue();
    }
}
