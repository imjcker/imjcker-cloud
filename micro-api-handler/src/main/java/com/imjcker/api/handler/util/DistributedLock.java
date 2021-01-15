package com.imjcker.api.handler.util;

import com.imjcker.api.common.util.RedisClusterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import java.util.Objects;
import java.util.UUID;

/**
 * @Author WT
 * @Date 15:47 2019/9/29
 * @Version DistributedLock v1.0
 * @Desicrption 分布式锁
 */
public class DistributedLock {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    public static String lockWithTimeout(String lockName, long acquireTimeout, long timeout) {
        String retIdentifier = null;
        try {
            String identifier = UUID.randomUUID().toString();
            String lockKey = "lock:" + lockName;
            // 超时时间
            int lockExpire = (int) timeout / 1000;

            // 设置获取锁的时间
            long end = System.currentTimeMillis() + acquireTimeout;
            while (System.currentTimeMillis() < end) {
                JedisCluster jedis = RedisClusterUtils.getJedis();
                if (jedis.setnx(lockKey, identifier) == 1) {
                    jedis.expire(lockKey, lockExpire);
                    retIdentifier = identifier;
                    return retIdentifier;
                }

                // 检查获取分布式锁的key是否设置超时时间
                if (jedis.ttl(lockKey) == -1) {
                    jedis.expire(lockKey, lockExpire);
                }

                // 休眠500ms
                Thread.sleep(500);
            }
        } catch (Exception e) {
            logger.error("发生异常:{}", e.getMessage());
        }
        return null;
    }

    public static boolean releaseLock(String lockName, String identifier) {
        String lockKey = "lock:" + lockName;
        JedisCluster jedis = RedisClusterUtils.getJedis();
        if (logger.isDebugEnabled())
            logger.debug("释放锁之前 值: {}", jedis.get(lockKey));
        String id = jedis.get(lockKey);
        Long del = null;
        if (identifier.equals(id)) {
            del = jedis.del(lockKey);
            if (logger.isDebugEnabled())
                logger.debug("释放锁 del:{}", del);
        }
        return Objects.equals(del, 1L);
    }
}
