package com.imjcker.manager.charge.scheduled.config;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;

/**
 * @Desicrption 定时配置
 */
@Configuration
public class SchedulerConfig {

    /**
     * 注入 jobFactory
     * @param applicationContext
     * @return
     */
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJonFactory jonFactory = new AutowiringSpringBeanJonFactory();
        jonFactory.setApplicationContext(applicationContext);
        return jonFactory;
    }

    /**
     * 注入 schedulerFactoryBean 以动态调度定时任务
     * @param jobFactory
     * @return
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setJobFactory(jobFactory);
        return factoryBean;
    }

    /**
     * 注入scheduler
     * @param schedulerFactoryBean
     * @return
     */
    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
        return schedulerFactoryBean.getScheduler();
    }



    /**
     * 此注入为静态注入
     * 注入调度器  注入 触发器,执行任务,可接受多个触发器
     * @param jobFactory
     * @param checkNetTrigger
     * @return
     * @throws IOException
     */
//    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory,
                             Trigger checkNetTrigger,Trigger secondTrigger)
            throws IOException {

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory);
        // 可以设置quartz 框架的执行线程池的配置
//        factory.setQuartzProperties(quartzProperties());
        // 注入任务触发器,可设置多个
        factory.setTriggers(checkNetTrigger,secondTrigger);

        return factory;
    }


}
