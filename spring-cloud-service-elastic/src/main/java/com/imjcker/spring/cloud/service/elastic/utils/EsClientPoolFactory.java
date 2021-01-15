package com.imjcker.spring.cloud.service.elastic.utils;


import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class EsClientPoolFactory implements PooledObjectFactory<RestHighLevelClient> {

    /**
     * 生产对象
     *
     * @return
     */
    @Override
    public PooledObject<RestHighLevelClient> makeObject() {
        RestHighLevelClient client = null;
        try {
            client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultPooledObject<>(client);
    }

    /**
     * 销毁对象
     *
     * @param p
     * @throws Exception
     */
    @Override
    public void destroyObject(PooledObject<RestHighLevelClient> p) throws Exception {
        RestHighLevelClient highLevelClient = p.getObject();
        highLevelClient.close();
    }

    @Override
    public boolean validateObject(PooledObject<RestHighLevelClient> p) {
        return true;
    }

    @Override
    public void activateObject(PooledObject<RestHighLevelClient> p) throws Exception {
        System.out.println("activateObject");
    }

    @Override
    public void passivateObject(PooledObject<RestHighLevelClient> p) throws Exception {
        System.out.println("passivateObject");
    }
}