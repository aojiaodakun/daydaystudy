package com.hzk.java.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpServerTest {


    private static Map<String, String> name2passwordMap = new HashMap<>();

    static {
        name2passwordMap.put("admin", "admin");
        name2passwordMap.put("admin1", "admin1");
        name2passwordMap.put("admin2", "admin2");

        name2passwordMap.put("hzk", "admin");
        name2passwordMap.put("hzk1", "admin");
        name2passwordMap.put("hzk2", "admin");

        name2passwordMap.put("user1", "1234567");
        name2passwordMap.put("user2", "1234567");

    }

    public static void main(String[] args) throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(3658), 0);
        httpServer.createContext("/test", new TestHandler());
        httpServer.start();
    }

    static class TestHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "login fail";

            String queryString =  exchange.getRequestURI().getQuery();
            Map<String,String> queryStringInfo = formData2Dic(queryString);
            if (queryStringInfo != null) {
                String name = queryStringInfo.get("name");
                String password = queryStringInfo.get("password");
                if (name2passwordMap.get(name) != null && name2passwordMap.get(name).equals(password)) {
                    response = "login success";
                }
            }


            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        public static Map<String,String> formData2Dic(String formData) {
            Map<String,String> result = new HashMap<>();
            if(formData== null || formData.trim().length() == 0) {
                return result;
            }
            final String[] items = formData.split("&");
            Arrays.stream(items).forEach(item ->{
                final String[] keyAndVal = item.split("=");
                if( keyAndVal.length == 2) {
                    try{
                        final String key = URLDecoder.decode( keyAndVal[0],"utf8");
                        final String val = URLDecoder.decode( keyAndVal[1],"utf8");
                        result.put(key,val);
                    }catch (UnsupportedEncodingException e) {}
                }
            });
            return result;
        }

    }
}
