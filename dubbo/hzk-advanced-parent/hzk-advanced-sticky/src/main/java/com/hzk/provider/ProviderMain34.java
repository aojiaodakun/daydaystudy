package com.hzk.provider;

import com.hzk.factory.ProviderFactory;
import com.hzk.service.DemoServiceImpl34_1;
import com.hzk.service.DemoServiceImpl34_2;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.dubbo.config.ServiceConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 34、粘滞连接
 */
public class ProviderMain34 {

    /**
     * 测试逻辑
     * 1、启动提供者，消费者；
     * 2、查看消费者一直访问的是服务1还是服务2
     * 3、通过http请求（http://localhost:8001/unexport?service=1）关闭服务，再查看消费者的控制台
     * 模拟场景如下：
     * 1、消费者一直在调用服务2
     * 2、http请求关闭服务2：http://localhost:8001/unexport?service=2
     * 3、消费者会一直调用服务1
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig1 = ProviderFactory.getCommonServiceConfig();
        serviceConfig1.setRef(new DemoServiceImpl34_1());
        serviceConfig1.getProtocol().setPort(20880);
        serviceConfig1.export();
        System.out.println("服务1导出完成");

        ServiceConfig serviceConfig2 = ProviderFactory.getCommonServiceConfig();
        serviceConfig2.setRef(new DemoServiceImpl34_2());
        serviceConfig2.getProtocol().setPort(20881);
        serviceConfig2.export();
        System.out.println("服务2导出完成");

        // http服务器，注销服务
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8001), 0);
        httpServer.createContext("/unexport", new TestHandler(serviceConfig1, serviceConfig2));
        httpServer.start();

        // 阻塞主线程
        new CountDownLatch(1).await();
    }


    static class TestHandler implements HttpHandler{

        ServiceConfig serviceConfig1;
        ServiceConfig serviceConfig2;

        public TestHandler(ServiceConfig serviceConfig1, ServiceConfig serviceConfig2){
            this.serviceConfig1 = serviceConfig1;
            this.serviceConfig2 = serviceConfig2;
        }


        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "default response";

            String queryString =  exchange.getRequestURI().getQuery();
            Map<String,String> queryStringInfo = formData2Dic(queryString);
            String service = queryStringInfo.get("service");
            if (service != null && !service.equals("")) {
                if (service.equals("1")) {
                    serviceConfig1.unexport();
                    response = "service1 unexport finish";
                } else if (service.equals("2")) {
                    serviceConfig2.unexport();
                    response = "service2 unexport finish";
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
