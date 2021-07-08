import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DatabaseServer {
    public static void main(String[] args){

        try {
            ServerSocket server = new ServerSocket(2000);
            while (true) {
                System.out.println("server is listening on port 2000");
                Socket socket = server.accept();
                System.out.println("connection established with " + socket);
                new ServerThread(socket).start();
            }
        }catch (Exception e){
            System.out.println(e);
        }


    }
}

