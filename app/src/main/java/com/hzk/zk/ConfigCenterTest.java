package com.hzk.zk;

public class ConfigCenterTest {

    public static void main(String[] args) {

        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration("127.0.0.1:2181", "/config");
        String esconfig = zookeeperConfiguration.getProperty("es.config");
        System.out.println(esconfig);


    }

}
