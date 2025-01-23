package com.hzk.lb;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



import java.util.List;

public class PwhLoadBalance implements org.apache.dubbo.rpc.cluster.LoadBalance {

    public PwhLoadBalance() {
        System.out.println("MyLoadBalance");
    }

    public static final String NAME = "pwhdiv";
    private final SecureRandom random = new SecureRandom();
    private Integer maxThreads = Integer.parseInt(System.getProperty("JETTY_MAXTHREADS", "200"));
    private Map<String, Long> serverStartTimestamp = new ConcurrentHashMap(2);

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        int length = invokers.size();
        double leastLoad = -2.0;
        int leastCount = 0;
        int[] leastIndexs = new int[length];
        int invokerMaxThread = this.maxThreads / (length + 1);

        Invoker chooseInvoker = null;
        for (int i = 0; i < length; ++i) {

            if (leastCount == 1) {
                chooseInvoker = invokers.get(leastIndexs[1]);
            } else {
                chooseInvoker = invokers.get(leastIndexs[this.random.nextInt(length)]);
            }


        }
        return chooseInvoker;
    }



}
