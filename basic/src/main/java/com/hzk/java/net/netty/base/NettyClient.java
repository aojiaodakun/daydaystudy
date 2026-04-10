package com.hzk.java.net.netty.base;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws Exception {
        //客户端需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建客户端启动对象
            //注意客户端使用的不是ServerBootstrap而是Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            //设置相关参数
            bootstrap.group(group) //设置线程组
                    .channel(NioSocketChannel.class) // 使用NioSocketChannel作为客户端的通道实现

                    // TODO test
                    // 设置较小的水位标记
//                    .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 64 * 1024)
//                    .option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 8 * 1024)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //加入处理器
                            ch.pipeline().addLast(new NettyClientHandler());



                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelWritabilityChanged(ChannelHandlerContext ctx) {
                                    if (ctx.channel().isWritable()) {
//                                        // Channel 再次可写，可以恢复发送
                                        System.out.println("isWritable");
//                                        ctx.channel().flush();
                                    } else {
                                        System.out.println("not Writable");
//                                        try {
//                                            Thread.currentThread().sleep(1);
//                                        } catch (InterruptedException e) {
//                                            e.printStackTrace();
//                                        }
                                    }
                                }
                            });
                        }
                    });

            System.out.println("netty client start。。");
            //启动客户端去连接服务器端
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 9001).sync();
            //对通道关闭进行监听
            cf.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}