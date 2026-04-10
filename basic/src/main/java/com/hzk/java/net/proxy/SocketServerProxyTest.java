package com.hzk.java.net.proxy;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class SocketServerProxyTest {

    private static final int PORT_OPEN = 8001;
    private static final int PORT_INTRA = 8002;

    private static Socket socketOpen;
    private static Socket socketIntra;

    public static void main(String[] args) {
        // 8001
        socketOpen = bindOpen();

        // 8001->8002
        new Thread(()->{
            ServerSocket serverSocketIntra = null;
            DataOutputStream out = null;
            try {
                serverSocketIntra = new ServerSocket(PORT_INTRA);
                socketIntra = serverSocketIntra.accept();
                out = new DataOutputStream(socketIntra.getOutputStream());
                while (true) {
                    Thread.sleep(1000);
                    out.writeUTF(UUID.randomUUID().toString());
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }


    private static Socket bindOpen(){
        ServerSocket serverSocket = null;
        Socket socketOpen = null;
        DataOutputStream out = null;
        try {
            serverSocket = new ServerSocket(PORT_OPEN);
            socketOpen = serverSocket.accept();
            out = new DataOutputStream(socketOpen.getOutputStream());
            while (true) {
                Thread.sleep(1000);
                out.writeUTF(UUID.randomUUID().toString());
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return socketOpen;
    }



}
