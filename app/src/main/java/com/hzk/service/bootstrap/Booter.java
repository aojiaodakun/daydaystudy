package com.hzk.service.bootstrap;

import com.hzk.service.bootstrap.embedjetty.EmbedJettyServer;
import com.hzk.zk.ZKFactory;
import com.hzk.zk.ZookeeperConfiguration;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.dubbo.common.extension.ExtensionLoader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Booter {

    static Map<String, CuratorFramework> zkPool = new HashMap<>();

    public static void main(String[] args) throws Exception {
        // zk客户端
        CuratorFramework client = ZKFactory.getZkClient("127.0.0.1:2181");
        byte[] bytes = client.getData()
                .forPath("/config/prop/webserver.type");
        String zkValue = new String(bytes);

        // 对/config/prop监听
        String configPath = "/config/prop";
        // 监听方式1
//        PathChildrenCache pathChildrenCache = new PathChildrenCache(client,configPath,true);
//        pathChildrenCache.start();
//        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
//            @Override
//            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
//                if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
//                    System.out.println(event.getData().getPath());
//                    System.out.println(new String(event.getData().getData()));
//                    String fullPath = event.getData().getPath();
//                    String key = fullPath.replace(configPath + "/", "");
//                    String value = new String(event.getData().getData());
//                    // 设置系统属性
//                    System.setProperty(key, value);
//                }
//            }
//        });
        // 监听方式2
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration("127.0.0.1:2181", configPath);
        Iterator<String> keyIt = zookeeperConfiguration.keys();
        while (keyIt.hasNext()) {
            String key = keyIt.next();
            String value = zookeeperConfiguration.getProperty(key);
            // 设置系统属性
            System.setProperty(key, value);
        }

        /**
         * 2、启动webServer
         */
        String webServerType = "jetty";
//        String webServerType = "tomcat";
//        String webServerType = System.getProperty("webserver.type", defaultWebServerType);
        if (zkValue != null && !zkValue.equals("")) {
         webServerType = zkValue;
        }
        // dubboSpi，先学习javaSpi
        BootServer bootServer = ExtensionLoader.getExtensionLoader(BootServer.class).getExtension(webServerType);
        bootServer.start(args);

        /**
         * 3、启动monitor，TODO
         */

    }


}
