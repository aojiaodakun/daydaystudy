package com.hzk.dubbo.lb;

import com.hzk.util.ConsistentHashingBucket;
import com.hzk.util.ConsistentHashingBucketWrapper;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * 一致性哈希负载均衡
 */
public class ConsistentHashingLoadBalance implements org.apache.dubbo.rpc.cluster.LoadBalance {

//    private LoadBalance healthLoadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("health");

    @Override
    public <T> Invoker select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        // appId
        String group = url.getParameter("group");
        List<String> hostList = new ArrayList<>(invokers.size());
        for(Invoker<T> tempInvoker : invokers) {
//            hostList.add(tempInvoker.getUrl().getHost());
            // test
            hostList.add(String.valueOf(tempInvoker.getUrl().getPort()));
        }
        String tenantId = "tenantA";
        ConsistentHashingBucketWrapper<String> hashingBucketWrapper = RpcConsistentHashingManager.getOrCreateHashingBucket(group, hostList);
        String targetHost = hashingBucketWrapper.getNextBucket(tenantId);
        // 找到目标调用者
        Invoker<T> targetInvoker = null;
        for(Invoker<T>  tempInvoker : invokers) {
//            if (targetHost.equals(tempInvoker.getUrl().getHost())){
            // test
            if (targetHost.equals(String.valueOf(tempInvoker.getUrl().getPort()))){
                targetInvoker = tempInvoker;
                break;
            }
        }
        if (targetInvoker == null) {
            throw new RuntimeException();
        }
        return targetInvoker;
    }

}
