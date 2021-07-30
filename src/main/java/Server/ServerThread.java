package Server;


import SQL.Authorization;
import SQL.AuthorizationHandler;
import SQL.QueryHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread{
    Socket socket;
    QueryHandler queryHandler;

    public ServerThread(Socket socket){this.socket = socket;}

    public void run(){
        PrintWriter out = null;
        BufferedReader in = null;
        System.out.println("running thread");
        try{
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Authorization authorization = getAuthorization(out, in);
            queryHandler = new QueryHandler(out, authorization);
            if(authorization.equals(Authorization.DENIED)){
                out.println("Connection terminated\n");
                return;
            }
            while(true){
                String query = in.readLine();
                if(query.equalsIgnoreCase("esc")) break;
                queryHandler.handleQuery(query);
            }
        }catch(
                IOException e){
            System.out.println(e);}
        finally{
            try{
                if(socket != null) socket.close();
                if(out != null) out.close();
                if(in != null) in.close();
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }

    public Authorization getAuthorization(PrintWriter out, BufferedReader in) throws IOException {

        String id = in.readLine();
        AuthorizationHandler authorizationHandler = AuthorizationHandler.getInstance();
        Authorization authorization = authorizationHandler.getAuthorization(id);
        out.println("Authorization: " + authorization + "\n");
        return authorization;
    }
}

