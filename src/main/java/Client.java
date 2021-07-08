import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client{

    public static void main(String[] args) {

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        Scanner sc = null;

            try {

                System.out.println("connection established");

                socket = new Socket("127.0.0.1", 2000);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                sc = new Scanner(System.in);

                sc.useDelimiter(";");
                String input = "";

                if(sc.hasNext()) input = sc.next();
                input = input.replace("\n", " ");
                out.println(input);




            } catch (Exception e) {
                System.out.println(e);
            } finally {
                try {
                    if (socket != null) socket.close();
                    if (out != null) out.close();
                    if (in != null) in.close();
                    if(sc != null) sc.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
    }


    }
