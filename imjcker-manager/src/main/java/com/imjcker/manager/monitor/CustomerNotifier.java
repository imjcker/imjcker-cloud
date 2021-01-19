package com.imjcker.manager.monitor;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomerNotifier extends AbstractStatusChangeNotifier {

    private static final Logger logger = LoggerFactory.getLogger(CustomerNotifier.class);

    public CustomerNotifier(InstanceRepository repository) {
        super(repository);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        String status = instance.getStatusInfo().getStatus();
        String name = instance.getRegistration().getName();
        String healthUrl = instance.getRegistration().getHealthUrl();
        switch (status) {
            case "DOWN":
                logger.info("服务: {} 健康检查未通过,url: {}", name, healthUrl);
            case "OFFLINE":
                logger.info("服务: {} 离线,url: {}", name, healthUrl);
            case "UP":
                logger.info("服务: {} 上线,url: {}", name, healthUrl);
            case "UNKNOWN":
                logger.info("服务: {} 未知异常,url: {}", name, healthUrl);
        }
        return null;
    }
}
