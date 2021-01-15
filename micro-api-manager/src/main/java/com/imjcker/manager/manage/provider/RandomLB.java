package com.imjcker.manager.manage.provider;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;

import java.util.List;
import java.util.Random;

public class RandomLB {
    /**
     * 随机获取zuul-proxy服务的url地址
     * @param serviceInstances zuul-proxy
     * @return url
     */
    public static String getServer(List<ServiceInstance> serviceInstances) {
        EurekaDiscoveryClient.EurekaServiceInstance serviceInstance = (EurekaDiscoveryClient.EurekaServiceInstance) serviceInstances.get(new Random().nextInt(serviceInstances.size()));
        String homePageUrl = serviceInstance.getInstanceInfo().getHomePageUrl();
        return homePageUrl.substring(0, homePageUrl.length() - 1);
    }
}
