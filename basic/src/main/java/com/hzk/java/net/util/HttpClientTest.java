package com.hzk.java.net.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class HttpClientTest {

    private static HttpClient staticHttpClient;

    public static void main(String[] args) throws Exception {
        // method2
//        initHttpClient();

//        String uri = "http://localhost:3658/test";
        String uri = "http://localhost:8090/ierp/hello";
        for (int i = 0; i < 1; i++) {
            new Thread(()->{
                String threadName = Thread.currentThread().getName();
                String result = "";
                try {
//                    result = HttpClientUtil.get(uri);


                    Map<String, String> hearMap = new HashMap<>();
                    hearMap.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8;");
//                    hearMap.put("hear1", StringUtils.repeat("a", 55));
//                    hearMap.put("Content-Length", String.valueOf(10000000));

                    Map<String, Object> bodyMap = new HashMap<>();
                    bodyMap.put("b", StringUtils.repeat("b", 1024));



                    result = HttpClientUtil.post(uri, hearMap, bodyMap);
                    System.out.println(threadName + " end");
                } catch (Exception e) {
                    System.out.println(threadName + " error");
                    e.printStackTrace();
                }
            },"thread_" + i).start();
        }
//        Thread.currentThread().sleep(1000* 10);
//        new Thread(()->{
//            while (true) {
//                try {
//                    Thread.currentThread().sleep(1000* 1);
//                    String result = null;
//                    try {
//                        result = get(staticHttpClient, uri);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        initHttpClient();
//                    }
//                    System.out.println(result);
//                } catch (Exception e){
//
//                }
//            }
//        },"testConn").start();

        System.in.read();
    }

    private static void initHttpClient() {
        // TODO，10次业务调度，执行一次ping-pong逻辑
        staticHttpClient = HttpClientBuilder.create()
                //DefaultConnectionKeepAliveStrategy是默认的判断超时时间策略，读取的是Keep-Alive:timeout=超时时间
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setMaxConnTotal(1)// TODO，待调整
                .useSystemProperties()
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectionRequestTimeout(5000)
                                .setConnectTimeout(5000)
                                .setSocketTimeout(5000).build()
                ).build();
    }

    public static String get(HttpClient client, String url) throws IOException, URISyntaxException {
        BufferedReader in = null;

        String content = null;
        try {
            // 实例化HTTP方法
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuilder sb = new StringBuilder();
            String line;
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            content = sb.toString();
        } catch (URISyntaxException | IOException e) {
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    //ignore
                }
            }
        }
        return content;
    }


}
