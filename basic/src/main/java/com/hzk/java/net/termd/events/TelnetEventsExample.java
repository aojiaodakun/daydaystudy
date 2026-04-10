package com.hzk.java.net.termd.events;

import io.termd.core.telnet.netty.NettyTelnetTtyBootstrap;
import io.termd.core.function.Consumer;
import io.termd.core.tty.TtyConnection;

import java.util.concurrent.TimeUnit;

/**
 * https://github.com/alibaba/termd/blob/master/src/examples/java/examples/events/TelnetEventsExample.java
 */
public class TelnetEventsExample {

    public synchronized static void main(String[] args) throws Throwable {
        NettyTelnetTtyBootstrap bootstrap = new NettyTelnetTtyBootstrap().setOutBinary(true).setHost("localhost").setPort(4000);
        bootstrap.start(new Consumer<TtyConnection>() {
            @Override
            public void accept(TtyConnection conn) {
                EventsExample.handle(conn);
            }
        }).get(10, TimeUnit.SECONDS);
        System.out.println("Telnet server started on localhost:4000");
        TelnetEventsExample.class.wait();
    }

}
