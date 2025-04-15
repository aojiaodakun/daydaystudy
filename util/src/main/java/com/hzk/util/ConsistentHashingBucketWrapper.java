package com.hzk.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 支持多节点轮询策略
 */
public class ConsistentHashingBucketWrapper<N> {

    private static final int providerSize = Integer.getInteger("rpc.lb.hash.provider.sizes", 2);

    static {
        // TODO 监听租户映射提供者1:N的参数变化，及时清空map
    }

    private ConsistentHashingBucket<N> consistentHashingBucket;
    // Map<tenantId_providerSize, RoundRobinBucket>
    private Map<String, RoundRobinBucket<N>> key2nodesMap = new ConcurrentHashMap<>();

    public ConsistentHashingBucketWrapper(ConsistentHashingBucket<N> consistentHashingBucket) {
        this.consistentHashingBucket = consistentHashingBucket;
    }

    public List<N> getNodeList() {
        return consistentHashingBucket.getBucketList();
    }

    public List<N> getBucketList(String key) {
        return getRoundRobinBucket(key).getBucketList();
    }

    public N getNextBucket(String key) {
        return getRoundRobinBucket(key).getNextBucket();
    }

    private RoundRobinBucket<N> getRoundRobinBucket(String key) {
        return key2nodesMap.computeIfAbsent(key + "_" + providerSize, k -> {
            List<N> nodeList = this.consistentHashingBucket.getBucket(key, providerSize);
            return new RoundRobinBucket<>(nodeList);
        });
    }

    // 轮询桶
    private static class RoundRobinBucket<N> {
        private List<N> bucketList;
        private AtomicInteger position = new AtomicInteger(0);// 轮询算子

        public RoundRobinBucket(List<N> bucketList) {
            this.bucketList = bucketList;
        }

        public N getNextBucket() {
            int currentPosition = position.getAndIncrement();
            int index = currentPosition % bucketList.size();
            // 处理整数溢出的情况
            if (currentPosition < 0) {
                position.set(0);
            }
            return bucketList.get(index);
        }

        public List<N> getBucketList() {
            return bucketList;
        }
    }

}
