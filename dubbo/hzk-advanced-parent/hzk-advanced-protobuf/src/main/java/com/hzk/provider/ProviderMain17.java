package com.hzk.provider;

import com.hzk.proto.GreeterGrpc;
import com.hzk.proto.HelloReply;
import com.hzk.proto.HelloRequest;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;


/**
 * 17、Protobuf
 */
public class ProviderMain17 {

    static {
        System.setProperty("dubbo.application.logger","log4j");
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(ProviderMain17.class);

    public static void main(String[] args) throws Exception{
        int port = 50051;
        //1.forPort 指定监听客户端请求的端口
        //2.创建我们的服务端实现类的实例GreeterImpl并将传递给构建器的addService方法
        //3.调用build （）并 start（）在构建器上为我们的服务创建和启动RPC服务器
        Server server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();
        LOGGER.info("Server stated , listener on port:" + port);
        //JVM关闭时调用的钩子
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public synchronized void start() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                server.shutdown();
                System.err.println("*** server shut down");
            }
        });
        server.awaitTermination();
    }

    private static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
        /**
         * @param request          请求
         * @param responseObserver 响应观察器
         */
        @Override
        public void sayHello(HelloRequest request,
                             StreamObserver<HelloReply> responseObserver) {
            LOGGER.info("requestName:" + request.getName());
            HelloReply reply = HelloReply.newBuilder()
                    .setMessage("Hello" + request.getName())
                    .build();
            //返回 reply数据
            responseObserver.onNext(reply);
            //指定完成gRPC的处理
            responseObserver.onCompleted();
        }
    }

}
