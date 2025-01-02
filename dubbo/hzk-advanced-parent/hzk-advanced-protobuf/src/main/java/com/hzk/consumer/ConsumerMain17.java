package com.hzk.consumer;

import com.hzk.proto.GreeterGrpc;
import com.hzk.proto.HelloReply;
import com.hzk.proto.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;

import java.util.concurrent.TimeUnit;
/**
 * 17、Protobuf
 */
public class ConsumerMain17 {
    public static final Logger LOGGER = LoggerFactory.getLogger(ConsumerMain17.class);
    //阻塞/同步 的stub(存根)
    private static GreeterGrpc.GreeterBlockingStub blockingStub;
    //非阻塞/异步 的stub
    private static GreeterGrpc.GreeterStub async;

    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the
     * greeting.
     */
    public static void main(String[] args) throws Exception{
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        blockingStub = GreeterGrpc.newBlockingStub(channel);
        async = GreeterGrpc.newStub(channel);

        String name = "world";
        LOGGER.info("Will try to greet" + name + "..");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response = null;
        try {
            //使用阻塞 stub调用
//            response = blockingStub.sayHello(request);

            //非阻塞/异步 的stub
            async.sayHello(request, new StreamObserver<HelloReply>(){
                @Override
                public void onNext(HelloReply helloReply) {
                    System.out.println(helloReply);
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onCompleted() {
                    System.out.println(1);
                }
            });

        } catch (StatusRuntimeException e) {
            LOGGER.info(String.format("rpc failed:%s", e.getStatus()));
        }
        LOGGER.info("Greeting: " + response.getMessage());


        // 关闭
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

}