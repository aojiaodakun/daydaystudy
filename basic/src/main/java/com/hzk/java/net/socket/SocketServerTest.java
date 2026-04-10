package com.hzk.java.net.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerTest {

    private static final int PORT = 8001;

    public static void main(String[] args) throws Exception{
        ServerSocket server = new ServerSocket(PORT);
        Socket socket = server.accept();
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        InputStream inputStream = socket.getInputStream();
        BufferedReader bin = new BufferedReader(new InputStreamReader(inputStream));

//        String info = null;
//        while((info=bin.readLine())!=null){
//            out.writeUTF(info);
//            System.out.println(info);
//        }

        char[] bytes = new char[1024];
        int result;

        while((result=bin.read(bytes))!=-1){
            String tempString = new String(bytes);
            out.writeUTF(tempString);
            System.out.println(tempString);
        }
//        String str;
//        while((str=bin.readLine())!=null){
//            out.write(str.getBytes());
//            System.out.println(str);
//        }

        System.out.println("SocketServerTest end");

    }


}
