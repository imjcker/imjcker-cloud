package com.imjcker.manager.scheduled.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author WT
 * @Date 16:18 2019/8/7
 * @Desicrption
 */
@Component
public class SpringBeanTypeUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        if (SpringBeanTypeUtils.applicationContext == null) {
            SpringBeanTypeUtils.applicationContext = applicationContext;
        }
    }

    /**
     * 获取某个接口的所有实现类
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeanMap(Class<T> tClass) {
        return applicationContext.getBeansOfType(tClass);
    }
}
