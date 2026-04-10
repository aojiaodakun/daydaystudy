package com.hzk.tool.jmeter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JMeterTest {

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);

    private static Map<String, String> user2passwordMap = new HashMap<>();

    static {
        user2passwordMap.put("admin", "admin");
        user2passwordMap.put("admin1", "admin1");

    }

    public static void main(String[] args) throws Exception {
        // 1、启动http服务
        // http://127.0.0.1:8081/test?user=admin&password=admin
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8081), 0);
        httpServer.createContext("/test", new TestHandler());
        httpServer.start();
        System.out.println("httpServer start success, now:" + LocalDateTime.now());
        // 2、启动jmeter脚本，调用http服务
        // windows
        ProcessBuilder builder = new ProcessBuilder(
                "D:\\tool\\apache-jmeter-5.2\\bin\\jmeter.bat",
                "-n",
                "-t", "D:\\project\\daydaystudy\\basic\\src\\main\\resources\\jmeter\\jmeter-http-test.jmx",
                "-l", "D:\\project\\daydaystudy\\basic\\src\\main\\resources\\jmeter\\jmeter-http-test.jtl"
        );
        THREAD_POOL.submit(()->{
            try {
                Process process = builder.start();
                // 阻塞等待结果
                int exitCode = process.waitFor();
                System.out.println("jmeter execute success,exitCode=" + exitCode + ", now:" + LocalDateTime.now());
            } catch (Exception e) {
                System.err.println("jmeter execute success, now:" + LocalDateTime.now());
                e.printStackTrace();
            }
        });

    }

    static class TestHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "login fail";
            String queryString =  exchange.getRequestURI().getQuery();
            Map<String,String> queryStringInfo = formData2Dic(queryString);
            String user = queryStringInfo.get("user");
            String password = queryStringInfo.get("password");
            if (user2passwordMap.get(user) != null && user2passwordMap.get(user).equals(password)) {
                response = "login success";
            } else {
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
