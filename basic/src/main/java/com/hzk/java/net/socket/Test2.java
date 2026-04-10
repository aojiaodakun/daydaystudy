package com.hzk.java.net.socket;

import java.io.*;
import java.net.Socket;

public class Test2 {

    public static void main(String[] args) throws Exception {

        Socket socket =new Socket("127.0.0.1",8001);
        OutputStream os = socket.getOutputStream();//字节输出流
        PrintWriter pw =new PrintWriter(os);//将输出流包装成打印流
        pw.write("用户名：admin；密码：admin");
        pw.flush();


        socket.shutdownOutput();
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String info = null;
        while((info=br.readLine())!=null){
            System.out.println("Hello,我是客户端，服务器说："+info);
        }

//4、关闭资源
        br.close();
        is.close();
        pw.close();
        os.close();
        socket.close();

    }

}
