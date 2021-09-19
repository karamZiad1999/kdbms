package com.atypon.client;

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
        Scanner in = null;
        Scanner sc = null;

        try {
            while(true) {
                socket = new Socket("127.0.0.1", 2000);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new Scanner(socket.getInputStream()).useDelimiter("end");
                sc = new Scanner(System.in).useDelimiter(";");

                String input = "";

                input = sc.next();
                input = input.trim().replaceAll("\\s+", " ");
                if(input.equals("esc")) break;
                out.println(input);

                String output = " ";

                if(in.hasNext()) output = in.next();

                System.out.println(output);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (socket != null) socket.close();
                if (out != null) out.close();
                if (in != null) in.close();
                if (sc != null) sc.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}







