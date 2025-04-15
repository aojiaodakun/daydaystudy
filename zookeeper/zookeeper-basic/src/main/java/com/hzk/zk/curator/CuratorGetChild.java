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

import java.util.List;

public class CuratorGetChild {

    CuratorFramework client;

    @Before
    public void before(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        client = CuratorFrameworkFactory.builder()
                .connectString(BasicConstants.IP_CLUSTER)
                .sessionTimeoutMs(1000 * 20)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    @After
    public void after(){
        client.close();
    }


    @Test
    public void getChild1() throws Exception{
        List<String> list = client.getChildren()
                .forPath("/get");
        list.stream().forEach(s -> System.out.println(s));
    }

    @Test
    public void getChild2() throws Exception{
        client.getChildren()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println(curatorEvent.getPath());
                        System.out.println(curatorEvent.getType());
                        List<String> children = curatorEvent.getChildren();
                        children.stream().forEach(s -> System.out.println(s));
                    }
                })
                .forPath("/get");
        Thread.sleep(1000 * 20);
        System.out.println("结束");
    }

}
