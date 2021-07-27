package Server;


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
            queryHandler = new QueryHandler(out);
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
}

