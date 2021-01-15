package com.imjcker.api.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Title: RedisClusterUtils.java
 * <p>Description: Redis Cluster 访问工具类
 * <p>Copyright: Copyright © 2016, CQzlll, All Rights Reserved.
 *
 * @author yy
 * @version 1.0
 */
public class RedisClusterUtils {

    private static Logger logger = LoggerFactory.getLogger(RedisClusterUtils.class);

    private static ReentrantLock lock = new ReentrantLock();

    /**
     * Jedis主从连接池
     */
    private static JedisCluster jedisCluster = null;
    /**
     * Redis服务器IP
     */
    private static String hosts;
    /**
     * 访问密码
     */
    private static String password;
    /**
     * 主名称
     */
    private static String masterName;
    /**
     * 超时时间
     */
    private static int timeout;
    /**
     * 可用连接实例的最大数目，默认值为8
     * 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
     */
    private static int maxTotal = 50;
    /**
     * 控制一个pool最多有多少个状态为idle(空闲的)的Jedis实例，默认值为8
     */
    private static int maxIdle = 8;
    /**
     * 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时
     * 如果超过等待时间，则直接抛出JedisConnectionException
     */
    private static int maxWaitMillis = -1;
    /**
     * 在borrow一个Jedis实例时，是否提前进行Validate操作
     * 如果为true，则得到的Jedis实例均是可用的
     */
    private static boolean testOnBorrow = false;

    /*static {
        try {
            String redisClusterPropertiesPath = new StringBuffer().append(GlobalVariable.environment).append("/redisSentinel.properties").toString();
            InputStream redisClusterIS = RedisClusterUtils.class.getClassLoader().getResourceAsStream(redisClusterPropertiesPath);
            Properties redisClusterProperties = new Properties();
            redisClusterProperties.load(redisClusterIS);
            hosts = redisClusterProperties.getProperty("redis.hosts");
            password = redisClusterProperties.getProperty("redis.password");
            masterName = redisClusterProperties.getProperty("redis.masterName");
            maxTotal = Integer.parseInt(redisClusterProperties.getProperty("redis.maxTotal"));
            maxIdle = Integer.parseInt(redisClusterProperties.getProperty("redis.maxIdle"));
            timeout = Integer.parseInt(redisClusterProperties.getProperty("redis.timeout"));
            maxWaitMillis = Integer.parseInt(redisClusterProperties.getProperty("redis.maxWaitMillis"));
            testOnBorrow = Boolean.parseBoolean(redisClusterProperties.getProperty("redis.testOnBorrow"));
            logger.info("初始化Redis Sentinel配置参数成功.");
        } catch (Exception ex) {
            logger.error("初始化Redis Sentinel配置参数失败.", ex);
        }

    }*/

    private RedisClusterUtils() {
        throw new RuntimeException("禁止实例化Redis Cluster访问工具类.");
    }

    /**
     * 初始化Redis连接池
     */
    private static void initialPool() {
        lock.lock();
        try {
            if (jedisCluster == null) {
                Set<HostAndPort> set = new HashSet<>();
                if (StringUtils.isNotBlank(hosts)) {
                    String[] hostAddr = hosts.split(",");
                    for (String addr : hostAddr) {
                        String[] host = addr.split(":");
                        set.add(new HostAndPort(host[0].trim(), Integer.parseInt(host[1].trim())));
                    }
                }
                jedisCluster = new JedisCluster(set, timeout, timeout, 5, password, new GenericObjectPoolConfig());
                logger.debug("创建Redis Cluster Pool 成功");
            }
        } catch (Exception e) {
            logger.error("创建Redis Cluster Pool 失败", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 设置系统停止时需要执行的任务
     */
    private static void setShutdownWork() {
        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        if (jedisCluster != null) {
                            jedisCluster.close();
                            jedisCluster = null;
                            logger.debug("关闭 Redis Cluster Pool 成功");
                        }
                    } catch (IOException e) {
                        logger.error("关闭 Redis Cluster Pool 失败");
                    }
                }
            });
            logger.debug("设置系统停止时关闭Redis Sentinel Pool的任务成功.");
        } catch (Exception e) {
            logger.error("设置系统停止时关闭Redis Sentinel Pool的任务失败.",e);
        }
    }

    /**
     * 从连接池获取jedis实例
     *
     * @return
     */
    public static JedisCluster getJedis() {
        try {
            RedisTemplate redisTemplate = SpringUtils.getBean("redisTemplate", RedisTemplate.class);
            Object nativeConnection = redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
            if (nativeConnection instanceof JedisCluster)
                return (JedisCluster) nativeConnection;
        } catch (Exception e) {
            logger.error("获取jedis失败:{}", e.getMessage());
        }
        return null;
    }
}
