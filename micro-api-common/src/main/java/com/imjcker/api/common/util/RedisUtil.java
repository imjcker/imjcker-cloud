package com.imjcker.api.common.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 处理Redis相关逻辑
 */
public class RedisUtil {

    private static int timeoutSeconds = 60 * 60;
    private static Logger log = LoggerFactory.getLogger(RedisUtil.class);

    /**
     * 存入Map<String,String> 类
     * @param key
     * @param map
     */
    public static void hmset(String key,Map<String,String> map) {
        JedisCluster jedisCluster = RedisClusterUtils.getJedis();
        jedisCluster.hmset(key, map);
        jedisCluster.expire(key, timeoutSeconds);
    }

    /**
     * 获取 Map<String,String>
     * @param key
     * @return
     */
    public static Map<String,String> hgetAll(String key) {
        JedisCluster jedisCluster = RedisClusterUtils.getJedis();
        Map<String, String> map = jedisCluster.hgetAll(key);
        if (map != null && map.size() > 0) {
            return map;
        }
        return null;
    }

    /**
     * 存入到缓存
     *
     * @param key
     * @param result
     */
    public static void set(String key, Object result) {
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
    public static void setToCaches(String key, Object result) {
        try {
            JedisCluster jedis = RedisClusterUtils.getJedis();
            String value = JSONObject.toJSONString(result);
            jedis.set(key,value);
            log.debug("存入redis缓存,key = {}",key);
        } catch (Exception e) {
            log.error("存入redis缓存出错,删除key = {}",key);
            delete(key);
        }
    }

    /**
     * 获取 list 集合
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getList(String key,Class<T> clazz) {
        try {
            RedisTemplate redisTemplate = (RedisTemplate)SpringUtils.getBean("redisTemplate");
            String value = redisTemplate.opsForValue().get(key).toString();
            if (value != null) {
                return JSONObject.parseArray(value, clazz);
            }
        } catch (Exception e) {
            log.error("获取 list 集合出错：", e);
        }
        return null;
    }

    /**
     * 存入到缓存，带超时时间
     *
     * @param key
     * @param result
     * @param timeout
     */
    public static void set(String key, Object result, Integer timeout) {
        JedisCluster jedis = RedisClusterUtils.getJedis();
        String value = JSONObject.toJSONString(result);
        if (timeout != -2)
            jedis.setex(key, timeout, value);
        else
            jedis.set(key, value);
    }
    /**
     * 冲缓存中取数据
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T get(String key, Class<T> clazz) {
        JedisCluster jedis = RedisClusterUtils.getJedis();
        String value = jedis.get(key);
        if (value != null) {
            return JSONObject.parseObject(value, clazz);
        }
        return null;
    }
    public static JSONObject get(String key) {
        try{
            JedisCluster jedis = RedisClusterUtils.getJedis();
            String value = jedis.get(key);
            if (value != null) {
                log.info("获取redis缓存,key = {}",key);
                return JSONObject.parseObject(value);
            }
        }catch (Exception e){
            log.info("获取redis缓存出错,返回null,key={}",key);
            return null;
        }
        return null;
    }
    public static Object getObject(String key) {
        try{
            JedisCluster jedis = RedisClusterUtils.getJedis();
            String value = jedis.get(key);
            if (value != null) {
                log.info("获取redis缓存,key = {}",key);
                return value;
            }
        }catch (Exception e){
            log.info("获取redis缓存出错,返回null,key={}",key);
            return null;
        }
        return null;
    }
    public static Long delete(String key) {
        JedisCluster jedis = RedisClusterUtils.getJedis();
        log.info("删除redis缓存,key = {}",key);
        return jedis.del(key);
    }
    public static TreeSet<String> keys(String pattern) {
        TreeSet<String> keys = new TreeSet<>();
        JedisCluster jedisCluster = RedisClusterUtils.getJedis();
        Map<String,JedisPool> clusterNodes=  jedisCluster.getClusterNodes();
        for (String k : clusterNodes.keySet()){
            log.debug("getting keys from {}",k);
            JedisPool jp = clusterNodes.get(k);
            try (Jedis connection = jp.getResource()) {
                keys.addAll(connection.keys(pattern));
            } catch (Exception e) {
                log.error("Exception：{}",e);
            }
        }
        return keys;
    }
    public static void keysDel(String pattern) {
        JedisCluster jedisCluster = RedisClusterUtils.getJedis();
        Map<String,JedisPool> clusterNodes=  jedisCluster.getClusterNodes();
        for (String k : clusterNodes.keySet()){
            log.debug("getting keys from {}",k);
            JedisPool jp = clusterNodes.get(k);
            try (Jedis connection = jp.getResource()) {
                Set<String> keys1 = connection.keys(pattern);
                keys1.forEach(connection::del);
            } catch (Exception e) {
                log.error("Exception：{}",e);
            }
        }
    }

    /**
     * @Description : 15天过期key
     * @Date : 2020/5/12 14:40
     * @Auth : qiuwen
     */
    public static void keyExpire15Days(String key){
        JedisCluster jedisCluster = RedisClusterUtils.getJedis();
        if (jedisCluster != null) {
            jedisCluster.expire(key,15 * 60 * 60 * 24);
        }
    }

}
