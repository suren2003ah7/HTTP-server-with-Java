package com.company;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8080);
            while (true){
                Socket s = server.accept();
                InputStream reader = s.getInputStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] arr = new byte[10];
                for (int c = reader.read(arr, 0, arr.length); c != -1; c = reader.read(arr, 0, arr.length)) {
                    buffer.write(arr, 0, c);
                    buffer.flush();
                    byte[] temp = buffer.toByteArray();
                    if (temp[temp.length - 1] == 10)
                        if (temp[temp.length - 2] == 13)
                            if (temp[temp.length - 3] == 10)
                                if (temp[temp.length - 4] == 13)
                                    break;
                }
                String t = buffer.toString();
                requestHandler(t, s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void requestHandler(String request, Socket s){
        String[] r = request.split(" ");
        String url = r[1];
        OutputStream writer = null;
        try {
            String response = "HTTP/1.1 200 OK \n Content-Length: 13 \n Connection: close \n\r\n\r not valid url";
            if (url.equals("/")){
                StringBuilder data = readFile("index.html");
                response = "HTTP/1.1 200 OK \n Content-Length: " + data.length() + " \n Connection: close \n\r\n\r" + data;
            }
            writer = s.getOutputStream();
            byte[] a = response.getBytes();
            writer.write(a);
            writer.flush();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StringBuilder readFile(String path){
        try{
            String[] temp = Files.readAllLines(Paths.get(path)).toArray(new String[0]);
            StringBuilder data = new StringBuilder();
            for(int i = 0; i < temp.length; i++){
                data.append(temp[i]);
            }
            return data;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return new StringBuilder();
    }
}

