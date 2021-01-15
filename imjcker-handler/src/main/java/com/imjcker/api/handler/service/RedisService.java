package com.imjcker.api.handler.service;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.util.RedisClusterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * 处理Redis相关逻辑
 */
@Slf4j
@Service
public class RedisService {
    private static final int timeoutSeconds = 60 * 60;
    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisService(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, Object result) {
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(result), timeoutSeconds, TimeUnit.SECONDS);
    }

    /**
     * 存入到缓存
     *
     * @param key    key
     * @param result value
     */
    public void setToCaches(String key, Object result) {
        try {
            String value = JSONObject.toJSONString(result);
            redisTemplate.opsForValue().set(key, value);
            log.debug("存入redis缓存,key = {}", key);
        } catch (Exception e) {
            log.error("存入redis缓存出错,删除key = {}", key);
            this.delete(key);
        }
    }

    /**
     * 存入到缓存，带超时时间
     *
     * @param key     key
     * @param result  value
     * @param timeout timeout
     */
    public void set(String key, Object result, Integer timeout) {
        String value = JSONObject.toJSONString(result);
        if (timeout != -2) {
            redisTemplate.opsForValue().set(key, value, timeout);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 冲缓存中去数据
     *
     * @param key   key
     * @param clazz class
     * @param <T>   type
     * @return type
     */
    public <T> T get(String key, Class<T> clazz) {
        if (redisTemplate.hasKey(key)) {
            Object value = redisTemplate.opsForValue().get(key);
            return JSONObject.parseObject(value.toString(), clazz);
        }
        return null;
    }

    public JSONObject get(String key) {
        try {
            String value = redisTemplate.opsForValue().get(key).toString();
            if (value != null) {
                return JSONObject.parseObject(value);
            }
        } catch (Exception e) {
            log.info("获取redis缓存出错,返回null,key={}", key);
            return null;
        }
        return null;
    }

    public void delete(String key) {
        log.debug("删除redis缓存,key = {}", key);
        redisTemplate.delete(key);
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
                log.info("Exception：{}", e.getMessage());
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
                log.info("删除key值Exception：{}", e.getMessage());
            }
        }
    }

    public void delRedisCacheByHttpPath(String httpPath) {
        String redisKeyEnv1 = new StringBuilder("api:env1:").append("httpPath:").append(httpPath).toString();
        String redisKeyEnv2 = new StringBuilder("api:env1:").append("httpPath:").append(httpPath).toString();
        delete(redisKeyEnv1);
        delete(redisKeyEnv2);
    }
}
