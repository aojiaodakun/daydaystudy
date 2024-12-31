package com.hzk.zk.curator;

import com.hzk.zk.constants.BasicConstants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorGet {

    CuratorFramework client;

    String namespace = "config";

    @Before
    public void before(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        client = CuratorFrameworkFactory.builder()
                .connectString(BasicConstants.IP)
                .sessionTimeoutMs(1000 * 20)
                .retryPolicy(retryPolicy)
//                .namespace(namespace)
                .build();
        client.start();
    }

    @After
    public void after(){
        client.close();
    }


    @Test
    public void get1() throws Exception{
        byte[] bytes = client.getData()
                .forPath("/config/prop/webserver.type");
        String value = new String(bytes);
        System.out.println(value);
    }

    @Test
    public void get2() throws Exception{
        String node = "2";
        Stat stat = new Stat();
        byte[] bytes = client.getData()
                .storingStatIn(stat)
                .forPath("/node" + node);
        System.out.println(stat.getVersion());
        System.out.println(new String(bytes));
    }

    @Test
    public void get3() throws Exception{
        String node = "3";
        byte[] bytes = client.getData()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println(curatorEvent.getPath());
                        System.out.println(curatorEvent.getType());
                    }
                })
                .forPath("/node" + node);
        System.out.println(new String(bytes));
    }

}
