package com.hzk.redis.pool.builder;

import com.hzk.redis.pool.JedisPool0;
import com.hzk.redis.pool.JedisPoolConfigGeneral;
import com.hzk.redis.pool.Pool0;
import com.hzk.redis.pool.Pool0Builder;
import com.hzk.redis.pool.bean.HostAndPort;
import com.hzk.redis.pool.bean.RedisInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.util.Pool;

import java.util.Iterator;

public class StandaloneBuilder extends AbstractPoolBuilder<Jedis> implements Pool0Builder<Jedis> {
    private static final Logger logger = LoggerFactory.getLogger(StandaloneBuilder.class);

    @Override
    public Pool0<Jedis> build(String url, boolean ssl) {
        RedisInfo redisInfo = RedisInfoParser.parseUrl(url);
        String host = null, password;
        int port = 6379;
        Iterator<HostAndPort> it = redisInfo.getHostAndPorts().iterator();
        if (it.hasNext()) {
            HostAndPort hostAndPort = it.next();
            host = hostAndPort.getHost();
            port = hostAndPort.getPort();
        }
        password = redisInfo.getPassword();
        // 获取默认的配置信息
        JedisPoolConfig config = new JedisPoolConfigGeneral().getPoolConfig();
        //Pool<Jedis> pool = new JedisPool(config, host, port, 5000,password);
        Pool<Jedis> pool = new JedisPool(config, host, port, connectionTimeout(), soTimeout(), password,
                Protocol.DEFAULT_DATABASE, null, ssl, null, null, null);
        return new JedisPool0<>(pool);
    }


}