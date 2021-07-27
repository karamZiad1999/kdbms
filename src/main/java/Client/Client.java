package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        Scanner sc = null;
        System.out.println("connection established");


        try {
            socket = new Socket("127.0.0.1", 2000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sc = new Scanner(System.in);

            sc.useDelimiter(";");
            String input = "";

        while(true){
            input = sc.next();
            input = input.trim().replace("\n", " ");
            out.println(input);
            StringBuilder finalString = new StringBuilder();

            if(input.equalsIgnoreCase("esc")) break;

             String output = "";

            while(true){
                finalString.append(output);
                finalString.append("\n");
                output = in.readLine();
                if(output.length() == 0){
                    break;
                }
            }
            System.out.println(finalString.toString());

        }

        }catch (Exception e) {
            System.out.println(e);
        }finally{

            try {
                if (socket != null) socket.close();
                if (out != null) out.close();
                if (in != null) in.close();
                if (sc != null) sc.close();
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }
}







