package com.hzk.java.net.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class SocketClientTest {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 20881;

    public static void main(String[] args) {
        Socket socket = null;
        DataInputStream dis = null;
        InputStream is = null;
        try {
            socket = new Socket(HOST, PORT);
            // 请求
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.write("help\r\n".getBytes("utf-8"));
            out.flush();
            // 响应
            is = socket.getInputStream();
            dis = new DataInputStream(is);
            while (true) {
                System.out.println("receive_msg:" + dis.readUTF());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
