package com.hzk.dubbo.lb;

import com.hzk.util.ConsistentHashingBucket;
import com.hzk.util.ConsistentHashingBucketWrapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcConsistentHashingManager {



    // Map<appId, ConsistentHashingBucket>
//    private static Map<String, ConsistentHashingBucket<String>> APPID_HASHINGBUCKET_MAP = new ConcurrentHashMap<>(32);


//    public static ConsistentHashingBucket getOrCreateHashingBucket(String appId, List<String> hostList) {
//        ConsistentHashingBucket consistentHashingBucket = APPID_HASHINGBUCKET_MAP.computeIfAbsent(appId, key -> new ConsistentHashingBucket(hostList));
//        List<String> bucketList = consistentHashingBucket.getBuckets();
//        // 缓存的hash桶未变化
//        if (bucketList.size() == hostList.size() && bucketList.containsAll(hostList)) {
//            return consistentHashingBucket;
//        }
//        // 重建此appId的hash环
//        synchronized (RpcConsistentHashingManager.class) {
//            consistentHashingBucket = new ConsistentHashingBucket(hostList);
//            APPID_HASHINGBUCKET_MAP.put(appId, consistentHashingBucket);
//            return consistentHashingBucket;
//        }
//    }


    private static Map<String, ConsistentHashingBucketWrapper<String>> APPID_HASHINGBUCKET_MAP = new ConcurrentHashMap<>(32);

    public static ConsistentHashingBucketWrapper<String> getOrCreateHashingBucket(String appId, List<String> hostList) {
        ConsistentHashingBucketWrapper<String> hashingBucketWrapper = APPID_HASHINGBUCKET_MAP.computeIfAbsent(appId,
                key -> new ConsistentHashingBucketWrapper<>(new ConsistentHashingBucket<>(hostList)));
        List<String> nodeList = hashingBucketWrapper.getNodeList();
        // 缓存的hash桶未变化
        if (nodeList.size() == hostList.size() && nodeList.containsAll(hostList)) {
            return hashingBucketWrapper;
        }
        // 重建此appId的hash环
        synchronized (RpcConsistentHashingManager.class) {
            hashingBucketWrapper = new ConsistentHashingBucketWrapper<>(new ConsistentHashingBucket<>(hostList));
            APPID_HASHINGBUCKET_MAP.put(appId, hashingBucketWrapper);
            return hashingBucketWrapper;
        }
    }

}
