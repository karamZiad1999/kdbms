import SQL.QueryTranslator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread{
    Socket socket;
    QueryTranslator queryTranslator;

    public ServerThread(Socket socket){this.socket = socket;}

    public void run(){
        PrintWriter out = null;
        BufferedReader in = null;
        queryTranslator = new QueryTranslator();
        System.out.println("running thread");

        try{
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while(true){
                String query = in.readLine();
                if(query.equalsIgnoreCase("esc")) break;
                queryTranslator.translateQuery(query);
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

