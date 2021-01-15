package com.imjcker.manager.charge.utils;

import com.lemon.common.dao.util.RedisClusterUtils;
import redis.clients.jedis.JedisCluster;

public class RedisLuaOperateUtil {

    /**
     * redis为某个key 减少值 操作
     * @param redisKey  本次操作的key
     * @param initValue  数据库初始化到redis的值
     * @param updateValue   本次修改的值
     * @return 0表示set成功  1表示失败， key值扣减为负
     */
    public static Integer deRedisBalance(String redisKey,String initValue,String updateValue){
        //描述key逻辑
        String rechargeScripts = "local operator_key = KEYS[1] \n" +
                "local recharge_value = ARGV[1] \n" +
                "local init_value = ARGV[2] \n" +
                "local is_exists = redis.call(\"EXISTS\", operator_key) \n" +
                "if is_exists == 1 then \n" +
                "   local old_value = redis.call(\"GET\", operator_key) \n" +
                "   local new_value = old_value - recharge_value \n"+
                "   if new_value < 0 then \n" +
                "       return 1 \n"+
                "   else\n"+
                "       redis.call(\"SET\", operator_key, new_value) \n" +
                "       return 0 \n" +
                "   end\n"+
                "else \n" +
                "   local new_value=init_value-recharge_value\n"+
                "   if new_value < 0 then \n"+
                "       return 1;"+
                "   else\n"+
                "       redis.call(\"SET\", operator_key, new_value) \n" +
                "       return 0 \n" +
                "   end\n"+
                "end";

        JedisCluster jedis = RedisClusterUtils.getJedis();
        Object eval = jedis.eval(rechargeScripts, 1, redisKey, updateValue, initValue);
        return Integer.parseInt(eval.toString());
    }

    /**
     * redis为某个key 增加值 操作
     * @param redisKey
     * @param initValue
     * @param updateValue
     * @return
     */
    public static Integer upRedisBalance(String redisKey,String initValue,String updateValue){
        String rechargeScripts = "local operator_key = KEYS[1] \n" +
                "local recharge_value = ARGV[1] \n" +
                "local init_value = ARGV[2] \n" +
                "local is_exists = redis.call(\"EXISTS\", operator_key) \n" +
                "if is_exists == 1 then \n" +
                "\tlocal old_value = redis.call(\"GET\", operator_key) \n" +
                "\tlocal new_value = old_value + recharge_value \n" +
                "\tredis.call(\"SET\", operator_key, new_value) \n" +
                "\treturn 0 \n" +
                "else \n" +
                "\tlocal init_amount = init_value + recharge_value \n" +
                "\tredis.call(\"SET\", operator_key, init_amount) \n" +
                "\treturn 0 \n" +
                "end\t";
        JedisCluster jedis = RedisClusterUtils.getJedis();
        Object eval = jedis.eval(rechargeScripts, 1, redisKey, updateValue, initValue);
        return Integer.parseInt(eval.toString());
    }

    public static void updateTodayRedisBalance(String todayKey, String yesterdayKey) {
        String scripts = "local today_key = KEYS[1] \n" +
                "local yesterday_key = KEYS[2]\n" +
                "local today_exists = redis.call(\"EXISTS\", today_key) \n" +
                "if today_exists == 1 then \n" +
                "\treturn 0 \n" +
                "else \n" +
                "\tlocal yesterday_exists = redis.call(\"EXISTS\", yesterday_key) \n" +
                "\tif yesterday_exists == 1 then \n" +
                "\t\tlocal yesterday_value = redis.call(\"GET\", yesterday_key) \n" +
                "\t\tredis.call(\"SET\", today_key, yesterday_value) \n" +
                "\telse \n" +
                "\t\treturn 0 \n" +
                "\tend\n" +
                "\treturn 0 \n" +
                "end ";
        JedisCluster jedis = RedisClusterUtils.getJedis();
        Object eval = jedis.eval(scripts, 2, todayKey, yesterdayKey);
    }
}
