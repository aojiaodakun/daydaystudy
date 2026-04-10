package com.hzk.java.net.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

/**
 * Caused by: org.eclipse.jetty.io.EofException: null
 *     at org.eclipse.jetty.io.ChannelEndPoint.flush(ChannelEndPoint.java:280)
 *     at org.eclipse.jetty.io.WriteFlusher.flush(WriteFlusher.java:422)
 *     at org.eclipse.jetty.io.WriteFlusher.write(WriteFlusher.java:277)
 *     at org.eclipse.jetty.io.AbstractEndPoint.write(AbstractEndPoint.java:381)
 *     at org.eclipse.jetty.server.HttpConnection$SendCallback.process(HttpConnection.java:831)
 *     at org.eclipse.jetty.util.IteratingCallback.processing(IteratingCallback.java:248)
 *     at org.eclipse.jetty.util.IteratingCallback.iterate(IteratingCallback.java:229)
 *     at org.eclipse.jetty.server.HttpConnection.send(HttpConnection.java:555)
 *     at org.eclipse.jetty.server.HttpChannel.sendResponse(HttpChannel.java:1014)
 *     at org.eclipse.jetty.server.HttpChannel.write(HttpChannel.java:1086)
 *     at org.eclipse.jetty.server.HttpOutput.channelWrite(HttpOutput.java:285)
 *     at org.eclipse.jetty.server.HttpOutput.channelWrite(HttpOutput.java:269)
 *     at org.eclipse.jetty.server.HttpOutput.write(HttpOutput.java:873)
 *     at java.io.BufferedOutputStream.write(BufferedOutputStream.java:122)
 *     at sun.nio.cs.StreamEncoder.writeBytes(StreamEncoder.java:221)
 *     at sun.nio.cs.StreamEncoder.implWrite(StreamEncoder.java:282)
 *     at sun.nio.cs.StreamEncoder.write(StreamEncoder.java:125)
 *     at sun.nio.cs.StreamEncoder.write(StreamEncoder.java:135)
 *     at java.io.OutputStreamWriter.write(OutputStreamWriter.java:220)
 *     at java.io.Writer.write(Writer.java:157)
 *     at kd.bos.actiondispatcher.ActionUtil.writeResponseJson(ActionUtil.java:119)
 *     at kd.bos.web.actions.FormAction.batchInvokeAction(FormAction.java:320)
 *     	... 73 common frames omitted
 * Caused by: java.io.IOException: Broken pipe
 *     at sun.nio.ch.FileDispatcherImpl.writev0(Native Method)
 *     at sun.nio.ch.SocketDispatcher.writev(SocketDispatcher.java:51)
 *     at sun.nio.ch.IOUtil.write(IOUtil.java:148)
 *     at sun.nio.ch.SocketChannelImpl.write(SocketChannelImpl.java:504)
 *     at java.nio.channels.SocketChannel.write(SocketChannel.java:502)
 *     at org.eclipse.jetty.io.ChannelEndPoint.flush(ChannelEndPoint.java:274)
 *     	... 94 common frames omitted
 */
public class BioBrokenPipeExample {

    public static void main(String[] args) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final int port = 8079;

        // 服务器线程
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("服务器: 接受连接，立即关闭");
                clientSocket.close();  // 立即关闭连接
                latch.countDown();     // 通知客户端可以开始写了
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "bio-server").start();

        // 客户端
        try (Socket socket = new Socket("localhost", port)) {
            OutputStream out = socket.getOutputStream();

            // 等待服务器关闭连接
            latch.await();

            // 短暂延迟确保TCP FIN包到达
            Thread.sleep(100);

            System.out.println("客户端: 尝试写入已关闭的连接");

            // 第一次写入可能会成功（因为TCP缓冲）
            out.write("第一次写入".getBytes());
            out.flush();
            System.out.println("客户端: 第一次写入成功");

            // 第二次写入应该会失败
            for (int i = 0; i < 5; i++) {
                try {
                    out.write(("尝试 " + (i+1)).getBytes());
                    out.flush();
                    System.out.println("客户端: 写入成功 " + (i+1));
                    Thread.sleep(100);
                } catch (IOException e) {
                    System.err.println("客户端: 捕获到异常: " + e.toString());
                    /**
                     * windows：java.net.SocketException: Software caused connection abort: socket write error
                     * linux：java.io.IOException: Broken pipe
                     */
                    if (e.getMessage() != null && e.getMessage().contains("Broken pipe")) {
                        System.err.println("成功捕获Broken pipe异常!");
                        return;
                    }
                }
            }
        }
        System.in.read();
    }

}
