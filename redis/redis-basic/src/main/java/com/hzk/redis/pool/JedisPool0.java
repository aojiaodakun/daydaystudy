package com.hzk.redis.pool;


import redis.clients.jedis.util.Pool;

public class JedisPool0<T> implements Pool0<T> {

    private Pool<T> jedisPool;

    public JedisPool0(Pool<T> jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public T getResource() {
        return jedisPool.getResource();
    }

}

