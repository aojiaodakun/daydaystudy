package com.hzk.redis.pool.builder;


import com.hzk.redis.pool.Pool0Builder;

import java.util.concurrent.TimeUnit;

public abstract class AbstractPoolBuilder<T> implements Pool0Builder<T> {

    public static final String REDIS_CLIENT_PROPERTY_PREFIX = "redis.client.";

    public static final String REDIS_CLIENT_PROPERTY_CONNECT_TIMEOUT = REDIS_CLIENT_PROPERTY_PREFIX + "connectionTimeout";

    public static final String REDIS_CLIENT_PROPERTY_SOCKET_TIMEOUT = REDIS_CLIENT_PROPERTY_PREFIX + "soTimeout";

    public static final int DEFAULT_TIME_OUT_MILLIS = (int) TimeUnit.SECONDS.toMillis(5);

    public int connectionTimeout() {
        return getTimeout(REDIS_CLIENT_PROPERTY_CONNECT_TIMEOUT);
    }

    public int soTimeout() {
        return getTimeout(REDIS_CLIENT_PROPERTY_SOCKET_TIMEOUT);
    }

    private static int getTimeout(String propertyName) {
        return Integer.getInteger(propertyName, DEFAULT_TIME_OUT_MILLIS);
    }
}

