package com.imjcker.manager;

import com.imjcker.manager.charge.plugin.queue.ExecutorServiceProperties;
import com.imjcker.manager.config.DocumentConfigurationProperties;
import com.lemon.common.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;


/**
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月10日 下午5:28:19
 */
@Slf4j
@EnableTransactionManagement
@MapperScan({"com.imjcker.*.*.mapper"})
@EnableCaching
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EnableConfigurationProperties({
        DocumentConfigurationProperties.class, ExecutorServiceProperties.class
})
public class WebBackendService implements CommandLineRunner {
    private final Scheduler scheduler;
    private final ApplicationContext applicationContext;

    @Autowired
    public WebBackendService(Scheduler scheduler, ApplicationContext applicationContext) {
        this.scheduler = scheduler;
        this.applicationContext = applicationContext;
    }

    /**
     * 防Xss攻击过滤器注册
     */
/*    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("XssFilter");
        registration.setOrder(1);
        return registration;
    }*/

    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }

    public static void main(String[] args) {
        SpringApplication.run(WebBackendService.class, args);
    }


    @Override
    public void run(String... strings) {
        log.info("启动参数：");
        for (String arg : strings) {
            log.info(arg);
        }
        try {
            log.info("开始加载后台任务");
            Map<String, JobDetail> jobDetailMap = applicationContext.getBeansOfType(JobDetail.class);
            Map<String, Trigger> triggerMap = applicationContext.getBeansOfType(Trigger.class);
            jobDetailMap.forEach(((jobDetailName, jobDetail) ->
                    triggerMap.forEach(((triggerName, trigger) -> {
                        if (jobDetail.getKey().getName().equalsIgnoreCase(trigger.getKey().getName())) {
                            try {
                                log.info("JobDetail:{} TriggerName:{}", jobDetailName, triggerName);
                                scheduler.scheduleJob(jobDetail, trigger);
                            } catch (SchedulerException e) {
                                log.error("注册任务失败", e);
                            }
                        }
                    }))
            ));
            scheduler.start();
        } catch (Exception e) {
            log.error("启动任务失败", e);
        }
    }
}
