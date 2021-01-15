//package com.imjcker.manager.notify;
//
//import de.codecentric.boot.admin.event.ClientApplicationEvent;
//import de.codecentric.boot.admin.model.Application;
//import de.codecentric.boot.admin.notify.AbstractStatusChangeNotifier;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
///**
// * @Author WT
// * @Date 15:06 2019/11/21
// * @Version CustomerNotifier v1.0
// * @Desicrption
// */
//@Component
//public class CustomerNotifier extends AbstractStatusChangeNotifier {
//
//    private static final Logger logger = LoggerFactory.getLogger(CustomerNotifier.class);
//
//    @Override
//    protected void doNotify(ClientApplicationEvent clientApplicationEvent) throws Exception {
//        Application application = clientApplicationEvent.getApplication();
//
//        String status = application.getStatusInfo().getStatus();
//        String name = application.getName();
//        String healthUrl = application.getHealthUrl();
//        switch (status) {
//            case "DOWN":
//                logger.info("服务: {} 健康检查未通过,url: {}", name, healthUrl);
//            case "OFFLINE":
//                logger.info("服务: {} 离线,url: {}", name, healthUrl);
//            case "UP":
//                logger.info("服务: {} 上线,url: {}", name, healthUrl);
//            case "UNKNOWN":
//                logger.info("服务: {} 未知异常,url: {}", name, healthUrl);
//        }
//    }
//}
