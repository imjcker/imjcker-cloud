package com.imjcker.api.handler.strategy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author thh  2019/7/22
 * @version 1.0.0
 **/
@Component
public class ApiStrategyFactory implements ApplicationContextAware {
    private static Map<ApiStrategyMapping, ApiStrategy> map = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ApiStrategy> beansOfType = applicationContext.getBeansOfType(ApiStrategy.class);
        beansOfType.forEach((key, value) -> {
            System.out.println(key + ":" + value);
            map.put(value.getStrategy(), value);
        });
    }

    public static <T extends ApiStrategy> T getStrategy(ApiStrategyMapping apiStrategyMapping) {
        return (T) map.get(apiStrategyMapping);
    }
}
