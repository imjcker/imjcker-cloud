package com.imjcker.manager.elastic.config;

import com.imjcker.manager.elastic.elasticsearch.CustomerBillClient;
import com.imjcker.manager.elastic.elasticsearch.DatasourceBillClient;
import com.imjcker.manager.elastic.elasticsearch.EsRestClient;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztzh_tanhh 2019/12/12
 **/
@Configuration
@ConditionalOnProperty(prefix = "spring.data.elasticsearch", name = "cluster-nodes")
public class LoggerConfiguration {
    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Bean
    public EsRestClient esRestClient() {
        String[] nodes = clusterNodes.split(",");
        List<HttpHost> httpHosts = new ArrayList<>();
        for (String s : nodes) {
            String[] host = s.split(":");
            HttpHost httpHost = new HttpHost(host[0], Integer.valueOf(host[1]), "http");
            httpHosts.add(httpHost);
        }
        return new EsRestClient(httpHosts.toArray(new HttpHost[0]));
    }

    @Bean
    public CustomerBillClient customerBillClient() {
        String[] nodes = clusterNodes.split(",");
        List<HttpHost> httpHosts = new ArrayList<>();
        for (String s : nodes) {
            String[] host = s.split(":");
            HttpHost httpHost = new HttpHost(host[0], Integer.valueOf(host[1]), "http");
            httpHosts.add(httpHost);
        }
        return new CustomerBillClient(httpHosts.toArray(new HttpHost[0]));
    }

    @Bean
    public DatasourceBillClient datasourceBillClient() {
        String[] nodes = clusterNodes.split(",");
        List<HttpHost> httpHosts = new ArrayList<>();
        for (String s : nodes) {
            String[] host = s.split(":");
            HttpHost httpHost = new HttpHost(host[0], Integer.valueOf(host[1]), "http");
            httpHosts.add(httpHost);
        }
        return new DatasourceBillClient(httpHosts.toArray(new HttpHost[0]));
    }
}
