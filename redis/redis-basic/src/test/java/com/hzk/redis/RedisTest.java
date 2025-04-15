package com.hzk.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisTest {

    private Jedis jedis;

    @Before
    public void before() {
        jedis = RedisFactory.getJedisClient("127.0.0.1:6379");
    }

    @Test
    public void pingTest() {
        String pong = jedis.ping();
        System.out.println(pong);
    }

    @Test
    public void stringTest() {
        jedis.set("k1", "v1");
        String v1 = jedis.get("k1");
        System.out.println(v1);
    }

    @Test
    public void hashTest() {
        String key = "allUser";
        jedis.hset(key, "zhangsan", "18");
        jedis.hset(key, "lisi", "19");
        String value1 = jedis.hget(key, "zhangsan");
        System.out.println(value1);
        Map<String, String> valueMap = jedis.hgetAll(key);
        System.out.println(valueMap);
    }

    @After
    public void after() {
        jedis.close();
    }

}
