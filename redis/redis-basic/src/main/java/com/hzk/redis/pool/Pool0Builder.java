package com.hzk.redis.pool;

@FunctionalInterface
public interface Pool0Builder<T> {
    Pool0<T> build(String url, boolean ssl);
}
