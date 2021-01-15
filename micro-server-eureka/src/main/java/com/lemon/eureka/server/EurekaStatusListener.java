package com.lemon.eureka.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author thh  2019/6/24
 * @version 1.0.0
 * 健康检查可以在注册中心实现,不需要单独的健康检查服务
 **/
@Component
public class EurekaStatusListener {
    private static final Logger log = LoggerFactory.getLogger(EurekaStatusListener.class);

    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        log.info("{}上线,实例ID:{}", event.getInstanceInfo().getAppName(), event.getInstanceInfo().getInstanceId());
    }

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        log.info("{}下线，实例ID:{}", event.getAppName(), event.getServerId());
    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        log.info("{}续约，实例ID:{}", event.getAppName(), event.getServerId());
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        log.info("注册中心启动成功:{}", event.getTimestamp());
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        if (event.getSource() instanceof EurekaServerConfigBean) {
            log.info("注册中心启动完成:{}", event.getSource());
        }
    }

}
