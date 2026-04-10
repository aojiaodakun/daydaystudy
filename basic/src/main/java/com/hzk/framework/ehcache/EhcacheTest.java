package com.hzk.framework.ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.ExpiryPolicy;

import java.time.Duration;

public class EhcacheTest {

    public static void main(String[] args) throws Exception {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withDefaultSizeOfMaxObjectGraph(Integer.MAX_VALUE)
                .build(false);
        cacheManager.init();

        String region = "bos";
        long maxItemSize = 10000;
        ResourcePoolsBuilder poolsBuilder = ResourcePoolsBuilder.heap(maxItemSize);
        CacheConfigurationBuilder<String, Object> configBuilder =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class, poolsBuilder);
        ExpiryPolicy<Object, Object> expiryPolicy = ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(3600));

        // 有debug堆栈打印
        CacheConfiguration<String, Object> cacheConfiguration = configBuilder.withExpiry(expiryPolicy).build();

        // 无debug堆栈打印
//        Serializer stringSerializer = new StringSerializer();
//        CacheConfiguration<String, Object> cacheConfiguration = configBuilder.withExpiry(expiryPolicy).withValueSerializer(stringSerializer).build();
        Class<Object> valueType = cacheConfiguration.getValueType();
        Cache<String, Object> cache = cacheManager.createCache(region, cacheConfiguration);


        cache.put("a", "a");
        Object a = cache.get("a");

        cache.put("b", 11L);
        Object b = cache.get("b");
        System.out.println(a);

    }

}
