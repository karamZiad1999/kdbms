package SQL;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class AuthorizationHandler{
    private static HashMap<String, Authorization> idAuthorizationMap;
    private static AuthorizationHandler authorizationHandler;

    private AuthorizationHandler(){
        idAuthorizationMap = new HashMap<String, Authorization>();
        initializeMap();
    }

    public static AuthorizationHandler getInstance(){
        if(authorizationHandler == null) authorizationHandler = new AuthorizationHandler();
        return authorizationHandler;
    }

    public Authorization getAuthorization(String id){
        Authorization authorization = idAuthorizationMap.get(id);
        if(authorization == null) return Authorization.DENIED;
        else return authorization;
    }

    private void initializeMap(){
        try(RandomAccessFile idLogReader = new RandomAccessFile("idLog.kdb", "rw")){
            String logEntry;
            while((logEntry = idLogReader.readLine()) != null){
                String [] entry;
                entry = logEntry.split(" ");
                Authorization authorization = parseAuthorization(entry[1]);
                if(authorization.equals(Authorization.DENIED)) continue;
                idAuthorizationMap.put(entry[0], authorization);
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    private Authorization parseAuthorization(String authorizationString){
        switch(authorizationString){
            case "READ_AND_WRITE":
                return Authorization.READ_AND_WRITE;
            case "DATABASE_MAINTAINER":
                return Authorization.DATABASE_MAINTAINER;
            case "READ_ONLY":
                return Authorization.READ_ONLY;
        }
        return Authorization.DENIED;
    }

}
