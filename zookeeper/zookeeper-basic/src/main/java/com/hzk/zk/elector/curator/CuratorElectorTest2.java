package com.hzk.zk.elector.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryOneTime;

public class CuratorElectorTest2 {


    public static void main(String[] args) throws Exception{
        String url = "172.20.70.40:2181,172.20.70.40:2182,172.20.70.40:2183";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(url)
                .retryPolicy(new RetryOneTime(1000 *3))
                .sessionTimeoutMs(1000 * 20)
                .retryPolicy(retryPolicy);
        CuratorFramework client = builder.build();
        client.start();


        CuratorElectorListenerAdapter listenerAdapter = new CuratorElectorListenerAdapter(client, "/leaderSelect", "clientB");
        listenerAdapter.start();

        System.in.read();
    }

}
