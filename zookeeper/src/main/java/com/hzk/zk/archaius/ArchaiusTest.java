package com.hzk.zk.archaius;

import java.util.Iterator;

public class ArchaiusTest {


    public static void main(String[] args) {
        ZookeeperConfiguration configuration = new ZookeeperConfiguration("", "/config/prop");
        // 子节点全部键值对，节点名为key
        Iterator<String> keys = configuration.keys();
        String property = configuration.getProperty("dubbo.protocol.port");
        System.out.println(property);

    }

}
