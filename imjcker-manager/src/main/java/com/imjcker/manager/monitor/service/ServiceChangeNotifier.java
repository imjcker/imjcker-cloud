package com.imjcker.spring.cloud.service.monitor.service;

import com.imjcker.spring.cloud.service.monitor.constant.ServiceStatus;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ServiceChangeNotifier extends AbstractStatusChangeNotifier {
    public ServiceChangeNotifier(InstanceRepository repository) {
        super(repository);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        String status = instance.getStatusInfo().getStatus();
        String type = event.getType();
        log.info(type);
        switch (status) {
            case ServiceStatus.DOWN:
                log.info("service: {} is down", instance.getInfo());
                break;
            case ServiceStatus.UP:
                log.info("service: {} is up", instance.getInfo());
                break;
            case ServiceStatus.OFFLINE:
                log.info("service: {} is offline", instance.getInfo());
                break;
            case ServiceStatus.UNKNOWN:
                log.info("service: {} status is unknown", instance);
                break;
        }
        return Mono.empty();
    }
}
