package com.hzk.zk.curator;

import com.hzk.zk.constants.BasicConstants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorSet {

    CuratorFramework client;

    String namespace = "curator";

    @Before
    public void before(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        client = CuratorFrameworkFactory.builder()
                .connectString(BasicConstants.IP)
                .sessionTimeoutMs(1000 * 20)
                .retryPolicy(retryPolicy)
                .namespace(namespace)
                .build();
        client.start();
    }

    @After
    public void after(){
        client.close();
    }

    @Test
    public void set1() throws Exception{
        String node = "jetty";
        client.setData()
                .forPath("/webserver/type",(node).getBytes());
    }


    @Test
    public void set2() throws Exception{
        String node = "2";
        client.setData()
                .withVersion(1)
                .forPath("/node" + node,("node" + node).getBytes());
    }

    @Test
    public void set3() throws Exception{
        String node = "3";
        client.setData()
                .withVersion(-1)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println(curatorEvent.getPath());
                        System.out.println(curatorEvent.getType());
                    }
                })
                .forPath("/node" + node,("node" + node).getBytes());
        Thread.sleep(1000 * 5);
        System.out.println("结束");
    }

}
