package com.hzk.redis;


import com.hzk.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class RedisProtocols {

    public static final String PROTOCOL_DELIMITER = ":";

    public static final String REDIS_STANDALONE = "redis";

    public static final String REDIS_SHARD = "shard";

    public static final String REDIS_SENTINEL = "sentinel";

    public static final String REDIS_CLUSTER = "cluster";

    public static final String XCACHE_HOSTING = "xcache_hosting";

    public static final String XCACHE_MSERVICE = "xcache_mservice";

    public static final Set<String> PROTOCOLS = new HashSet<>();

    static {
        PROTOCOLS.add(REDIS_STANDALONE);
        PROTOCOLS.add(REDIS_SHARD);
        PROTOCOLS.add(REDIS_SENTINEL);
        PROTOCOLS.add(REDIS_CLUSTER);
        PROTOCOLS.add(XCACHE_HOSTING);
        PROTOCOLS.add(XCACHE_MSERVICE);
    }


    public static boolean isXCache(String url) {
        Pair<String, String> protocolAndUrl = getProtocolAndUrl(url);
        return (XCACHE_MSERVICE.equals(protocolAndUrl.getKey()) || XCACHE_HOSTING.equals(protocolAndUrl.getKey()));
    }

    public static Pair<String, String> getProtocolAndUrl(String url) {
        if (url == null) {
            return new Pair<>(REDIS_STANDALONE, null);
        }
        int i = url.indexOf(PROTOCOL_DELIMITER);
        String protocol = i > 0 ? url.substring(0, i).toLowerCase().trim() : null;
        String poolUrl = i >= 0 ? url.substring(i + 1) : url;
        if (!PROTOCOLS.contains(protocol)) {
            protocol = REDIS_STANDALONE;
            poolUrl = url;
        }
        return new Pair<>(protocol, poolUrl);
    }
}

