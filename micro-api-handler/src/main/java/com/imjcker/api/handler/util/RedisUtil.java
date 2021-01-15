package com.imjcker.api.handler.util;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.util.RedisClusterUtils;
import com.imjcker.api.common.vo.LogPojo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

/**
 * 处理Redis相关逻辑
 */
public class RedisUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);
    private static int timeoutSeconds = 60 * 60;

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
     * 冲缓存中去数据
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

    public Long delete(String key) {
        JedisCluster jedis = RedisClusterUtils.getJedis();
        return jedis.del(key);
    }

    public static void setToCache(Integer apiId, String appkey, String md5, String result, Integer cacheTime, String uniqueUuid) {
        String key = getResultKey(apiId, appkey, md5, uniqueUuid);
        LOGGER.debug("存入redis缓存，key：{}，数据：{}", key, result);
        set(key, result, cacheTime);
    }

    public static String getResultKey(Integer apiId, String appkey, String md5, String uniqueUuid) {
        return StringUtils.join(Constant.REDIS_KEY_RESULT, apiId, ":", appkey, ":", md5);
    }

    public static void setResultToCache(Integer apiId, String appkey, String md5, String result, Integer cacheTime, String uniqueUuid, String type) {
        try {
            setToCache(apiId, appkey, md5, result, cacheTime, uniqueUuid);
        } catch (Exception e) {
            LOGGER.error(LogPojo.getErrorLogMsg("存入Redis出错", result, e));
        }
    }

    public static String getResultFromCache(Integer apiId, String appkey, String md5, String uniqueUuid) {
        String key = getResultKey(apiId, appkey, md5, uniqueUuid);
        try {
            String result = RedisUtil.get(key, String.class);
            LOGGER.info("读取redis缓存,key: {}, 数据: {}", key, result);
            return result;
        } catch (Exception e) {
            LOGGER.error(LogPojo.getErrorLogMsg("读取Redis出错", key, e));
        }
        return null;
    }
}
