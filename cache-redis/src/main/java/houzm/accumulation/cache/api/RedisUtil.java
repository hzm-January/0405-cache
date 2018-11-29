package houzm.accumulation.cache.api;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Package: houzm.accumulation.cache.api
 * Author: hzm_dream@163.com
 * Date: Created in 2018/11/9 20:13
 * Copyright: Copyright (c) 2018
 * Version: 0.0.1
 * Modified By:
 * Description： Jedis Api
 */
public class RedisUtil {

    private final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    private static final String ipAddr = "192.168.1.111";
    private static final String port = "6379";
    private static Jedis jedis = null;

    /**
     * redis util 单例
     */
    private RedisUtil() {
    }

    private static class RedisHolder {
        private final static RedisUtil JEDIS_API = new RedisUtil();
    }

    private static RedisUtil jedis() {
        return RedisHolder.JEDIS_API;
    }

    /**
     * 连接池
     */
    private static Map<String, JedisPool> maps = new HashMap<>();

    /**
     * 连接池
     * @param ip
     * @param port
     * @return
     */
    private static JedisPool jedisPool(String ip, int port) {
        String key = ip + ":" + port;
        JedisPool pool = null;
        if (!maps.containsKey(key)) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(RedisConfig.MAX_IDLE);
            config.setMaxWaitMillis(RedisConfig.MAX_WAIT);
            config.setMaxTotal(RedisConfig.MAX_ACTIVE);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            pool = new JedisPool(config, ip, port, RedisConfig.TIMEOUT);
            maps.put(key, pool);
        } else {
            pool = maps.get(key);
        }
        return pool;
    }

    /**
     * 从池中获取连接
     * @param ip
     * @param port
     * @return
     */
    public static Jedis getJedis(String ip, int port) {
        Jedis jedis = null;
        int count = 0;
        do {
            try {
                jedis = jedisPool(ip, port).getResource();
            } catch (Exception e) {
                e.printStackTrace();
                if (jedis != null) {
                    jedis.close();
                }
            }
            count++;
        } while (jedis == null && count < RedisConfig.RETRY_NUM);
        return jedis;
    }

    /**
     * 归还连接
     * @param jedis
     */
    public void close(Jedis jedis) {
        jedis.close();
    }


    private static class RedisConfig {
        //可用连接实例的最大数目，默认值为8；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        public static final int MAX_ACTIVE = 1024;

        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
        public static final int MAX_IDLE = 200;

        //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
        public static final int MAX_WAIT = 10000;

        public static final int TIMEOUT = 10000;

        public static final int RETRY_NUM = 5;
    }
}
