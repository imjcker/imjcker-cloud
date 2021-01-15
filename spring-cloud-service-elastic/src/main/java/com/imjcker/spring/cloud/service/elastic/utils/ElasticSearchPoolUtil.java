package com.imjcker.spring.cloud.service.elastic.utils;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticSearchPoolUtil {

    // 对象池配置类, 不写也可以, 采用默认配置
    private static GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

    // 配置maxTotal是8, (默认配置也是8,写在这里是为了以后如果要增加可以修改这里)  池中有8个client
    static {
        poolConfig.setMaxTotal(8);
    }

    // 要池画的对象的工厂类
    private static EsClientPoolFactory esClientPoolFactory = new EsClientPoolFactory();

    // 利用对象工厂和配置类生成对象池
    private static GenericObjectPool<RestHighLevelClient> clientPool = new GenericObjectPool<>(esClientPoolFactory, poolConfig);

    /**
     * 获得对象
     *
     * @return
     * @throws Exception
     */
    public static RestHighLevelClient getClient() throws Exception {
        return clientPool.borrowObject();
    }

    /**
     * 归还对象
     *
     * @param client
     */
    public static void returnClient(RestHighLevelClient client) {
        clientPool.returnObject(client);
    }
}
