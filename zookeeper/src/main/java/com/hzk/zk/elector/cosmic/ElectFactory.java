package com.hzk.zk.elector.cosmic;

//import com.hzk.zk.elector.cosmic.impl.redis.RedisCompeteElectorImpl;
import com.hzk.zk.elector.cosmic.impl.zookeeper.ZKCompeteElectorImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ElectFactory {
    private static Map<String, Elector> electors = new ConcurrentHashMap<>(2);
    private static String ELECT_TYPE = "elect.type";

    public static Elector getElector(String electClusterName) {
        String type = System.getProperty(ELECT_TYPE, "zookeeper");
        if ("redis".equals(type)) {
//            return electors.computeIfAbsent(electClusterName, k -> new RedisCompeteElectorImpl(electClusterName));
            return null;
        } else if ("zookeeper".equals(type)) {
            return electors.computeIfAbsent(electClusterName, k -> new ZKCompeteElectorImpl(k));
        } else {
            throw new RuntimeException();
        }
    }
}

