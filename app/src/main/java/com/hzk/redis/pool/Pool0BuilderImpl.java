package com.hzk.redis.pool;

import com.hzk.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.hzk.redis.pool.builder.StandaloneBuilder;
import static com.hzk.redis.RedisProtocols.*;

public class Pool0BuilderImpl {

    private static final Logger logger = LoggerFactory.getLogger(Pool0BuilderImpl.class);

    private static final Map<String, Supplier<Pool0Builder>> protocolAndBuilders = new HashMap<>();

    static {
        protocolAndBuilders.put(REDIS_STANDALONE, StandaloneBuilder::new);
//        protocolAndBuilders.put(REDIS_SHARD, ShardBuilder::new);
//        protocolAndBuilders.put(REDIS_SENTINEL, SentinelBuilder::new);
//        protocolAndBuilders.put(REDIS_CLUSTER, ClusterBuilder::new);
    }

    public static Pool0 getPool(String url)  {
        if (StringUtils.isBlank(url)) {
            logger.error(url);
            throw new NullPointerException("Redis url can not be null!!");
        }
        Pair<String, String> protocolAndUrl = getProtocolAndUrl(url);
        Pool0Builder<?> factory = protocolAndBuilders.containsKey(protocolAndUrl.getKey()) ? protocolAndBuilders.get(protocolAndUrl.getKey()).get() : new StandaloneBuilder();
        String poolUrl = protocolAndUrl.getValue();

        //使用格式为： System.setProperty("redis.serversForCache", "redis:ssl:ip:port/password");
        boolean ssl = poolUrl.toLowerCase().startsWith("ssl:");
        poolUrl = ssl ? poolUrl.substring(4) : poolUrl;
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Protocol: [%s], url: [%s], ssl: [%s], poolUrl: [%s]", protocolAndUrl.getKey(), url, ssl, poolUrl));
        }
        return factory.build(poolUrl, ssl);
    }
}
