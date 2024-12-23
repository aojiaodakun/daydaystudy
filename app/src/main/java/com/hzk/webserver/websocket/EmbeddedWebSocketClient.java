package com.hzk.webserver.websocket;

import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class EmbeddedWebSocketClient {

    public static void main(String[] args) throws Exception {
        URI uri = new URI("ws://localhost:8888/echo");
        WebSocketContainer wsContainer = ContainerProvider.getWebSocketContainer();
// 连接websocket服务端
        Session session = wsContainer.connectToServer(new EmbeddedWebSocketClient(), uri);
        try {
            Whole<String> handler = new ClientMessageHandler();
            session.addMessageHandler(handler);
// 发送三条消息
            session.getBasicRemote().sendText("echo hello1");
            session.getBasicRemote().sendText("echo hello2");
            session.getBasicRemote().sendText("echo hello3");
            Thread.sleep(5000l);
        } finally {
            session.close();
        }
    }
    public static class ClientMessageHandler implements Whole<String>{
        @Override
        public void onMessage(String message) {
// 接收消息并打印
            System.out.println("client send: " + message);
        }
    }

}
