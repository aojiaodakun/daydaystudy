package com.hzk.redis;

import com.hzk.redis.pool.Pool0;
import com.hzk.redis.pool.Pool0BuilderImpl;
import com.hzk.redis.pool.builder.RedisInfoParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisFactory {

    private static final Logger logger = LoggerFactory.getLogger(RedisFactory.class);
    private static ConcurrentHashMap<String, Pool0<Jedis>> poolMap = new ConcurrentHashMap<>();

    public static Jedis getJedisClient(String url) {
        Pool0<Jedis> pool = poolMap.computeIfAbsent(url, key -> {
            try {
                return Pool0BuilderImpl.getPool(key);
            } catch (Exception e) {
                /*throw new KDException(e, BosErrorCode.bOS, Resources.getString("初始化JedisPool错误", "RedisFactory_1", "bos-redis"));*/
                throw new RuntimeException(e);
            }
        });

        try {
            Object resource = pool.getResource();
            return (Jedis)resource;
        } catch (Exception e) {
            String msg = null;
            Throwable sub = e;
            while (sub != null && sub.getCause() != null) {
                sub = sub.getCause();
            }
            if (sub != null) {
                msg = "Get JedisClient error: " + sub.getMessage() + ", url: [" + RedisInfoParser.trimUrl(url) + "]";
            }
            throw e;
        }
    }

}
