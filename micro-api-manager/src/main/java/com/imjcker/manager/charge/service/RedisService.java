package com.imjcker.manager.charge.service;

import com.alibaba.fastjson.JSONObject;
import com.lemon.common.dao.util.RedisClusterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 处理Redis相关逻辑
 */
@Service
public class RedisService {

    private static int timeoutSeconds = 60 * 60;
    private Logger log = LoggerFactory.getLogger(RedisService.class);

    /**
     * 存入到缓存
     *
     * @param key
     * @param result
     */
    public void set(String key, Object result) {
        JedisCluster jedis = RedisClusterUtils.getJedis();
        String value = JSONObject.toJSONString(result);
        jedis.setex(key, timeoutSeconds, value);
    }

    /**
     * 存入到缓存
     *
     * @param key
     * @param result
     */
    public void setToCaches(String key, Object result) {
        try {
            JedisCluster jedis = RedisClusterUtils.getJedis();
            String value = JSONObject.toJSONString(result);
            jedis.set(key, value);
            log.info("存入redis缓存,key = {}", key);
        } catch (Exception e) {
            log.info("存入redis缓存出错,删除key = {}", key);
            this.delete(key);
        }
    }

    /**
     * 存入到缓存，带超时时间
     *
     * @param key
     * @param result
     * @param timeout
     */
    public void set(String key, Object result, Integer timeout) {
        JedisCluster jedis = RedisClusterUtils.getJedis();
        String value = JSONObject.toJSONString(result);
        if (timeout != -2)
            jedis.setex(key, timeout, value);
        else
            jedis.set(key, value);
    }

    /**
     * 冲缓存中去数据
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {
        JedisCluster jedis = RedisClusterUtils.getJedis();
        String value = jedis.get(key);
        if (value != null) {
            return JSONObject.parseObject(value, clazz);
        }
        return null;
    }

    public JSONObject get(String key) {
        try {
            JedisCluster jedis = RedisClusterUtils.getJedis();
            String value = jedis.get(key);
            if (value != null) {
//                log.info("获取redis缓存,key = {}", key);
                return JSONObject.parseObject(value);
            }
        } catch (Exception e) {
            log.info("获取redis缓存出错,返回null,key={}", key);
            return null;
        }
        return null;
    }

    public Long delete(String key) {
        JedisCluster jedis = RedisClusterUtils.getJedis();
        log.info("删除redis缓存,key = {}", key);
        return jedis.del(key);
    }

    public TreeSet<String> keys(String pattern) {
        TreeSet<String> keys = new TreeSet<>();
        JedisCluster jedisCluster = RedisClusterUtils.getJedis();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for (String k : clusterNodes.keySet()) {
            log.info("getting keys from {}", k);
            JedisPool jp = clusterNodes.get(k);
            try (Jedis connection = jp.getResource()) {
                keys.addAll(connection.keys(pattern));
            } catch (Exception e) {
                log.info("Exception：{}", e);
            }
        }
        return keys;
    }

    public void keysDel(String pattern) {
        JedisCluster jedisCluster = RedisClusterUtils.getJedis();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for (String k : clusterNodes.keySet()) {
            JedisPool jp = clusterNodes.get(k);
            try (Jedis connection = jp.getResource()) {
                Set<String> keys1 = connection.keys(pattern);
                keys1.forEach(connection::del);
            } catch (Exception e) {
                log.info("删除key值Exception：{}", e);
            }
        }
    }

}
