package com.hzk.java.net.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;

public class SocketClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("start," + LocalDateTime.now());
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", 9000), 100);
            //向服务端发送数据
            socket.getOutputStream().write("HelloServer".getBytes());
            socket.getOutputStream().flush();
            System.out.println("向服务端发送数据结束");
            byte[] bytes = new byte[1024];
            //接收服务端回传的数据
            socket.getInputStream().read(bytes);
            System.out.println("接收到服务端的数据：" + new String(bytes));
            socket.close();
        } catch (Exception e) {
            System.out.println("error," + LocalDateTime.now());
        }
    }
}