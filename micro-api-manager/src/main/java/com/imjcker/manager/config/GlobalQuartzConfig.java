package com.imjcker.manager.config;

import com.imjcker.manager.health.config.HealthCheckProperties;
import com.imjcker.manager.health.service.HealthCheckService;
import com.imjcker.manager.manage.job.FileCleanupJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ztzh_tanhh 2019/11/27
 **/
@Slf4j
@EnableScheduling
@Configuration
@EnableConfigurationProperties({
        HealthCheckProperties.class})
public class GlobalQuartzConfig {
    private final HealthCheckProperties healthCheckProperties;

    @Autowired
    public GlobalQuartzConfig(HealthCheckProperties healthCheckProperties) {
        this.healthCheckProperties = healthCheckProperties;
    }
/*
    schedule 包下已经配置
    @Bean
    public Scheduler scheduler() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        return schedulerFactory.getScheduler();
    }*/

// 健康检查任务
    @Bean
    public JobDetail healthCheckJobDetail() {
        return JobBuilder.newJob(HealthCheckService.class)
                .withIdentity(healthCheckProperties.getName())
                .build();
    }

    @Bean
    public Trigger healthCheckTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(healthCheckProperties.getName())
                .withSchedule(CronScheduleBuilder.cronSchedule(healthCheckProperties.getCron()))
                .startNow()
                .build();
    }

// 临时文件清理任务
    @Bean
    public JobDetail cleanupJobDetail() {
        return JobBuilder.newJob(FileCleanupJob.class)
                .withIdentity("cleanup")
                .build();
    }

    @Bean
    public Trigger cleanupTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity("cleanup")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 8 * * ?"))
                .startNow()
                .build();
    }
}
