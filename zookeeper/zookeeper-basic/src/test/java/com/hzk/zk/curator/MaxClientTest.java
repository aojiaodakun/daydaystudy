package com.hzk.zk.curator;

import com.hzk.zk.constants.BasicConstants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.retry.RetryUntilElapsed;
import org.junit.Test;

public class MaxClientTest {

    @Test
    public void maxClientTest() throws Exception {
        System.setProperty("curator-dont-log-connection-problems", "false");

        // 每3秒重连一次，重试3次
        RetryPolicy retryPolicy = new RetryNTimes(3,1000 * 3);
        // 创建连接对象
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(BasicConstants.IP)
                .sessionTimeoutMs(1000 * 10)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        System.out.println(client.isStarted());

        byte[] bytes = client.getData()
                .forPath("/config/prop/webserver.type");
        String value = new String(bytes);
        System.out.println(value);

//        // 创建连接对象
//        CuratorFramework client1 = CuratorFrameworkFactory.builder()
//                .connectString(BasicConstants.IP)
//                .sessionTimeoutMs(1000 * 10)
//                .retryPolicy(retryPolicy)
//                .build();
//        client1.start();
//        System.out.println(client1.isStarted());
        System.in.read();
    }

}
