package com.hzk.java.net.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Test1 {

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket =new ServerSocket(8001);
        Socket socket = serverSocket.accept();
        InputStream is = socket.getInputStream();
        InputStreamReader isr =new InputStreamReader(is);
        BufferedReader br =new BufferedReader(isr);
        String info =null;
        while((info=br.readLine())!=null){
            System.out.println("Hello,我是服务器，客户端说："+info);
        }
        socket.shutdownInput();//关闭输入流
//4、获取输出流，响应客户端的请求
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os);
        pw.write("Hello World！");
        pw.flush();


//5、关闭资源
        pw.close();
        os.close();
        br.close();
        isr.close();
        is.close();
        socket.close();
        serverSocket.close();

    }

}
