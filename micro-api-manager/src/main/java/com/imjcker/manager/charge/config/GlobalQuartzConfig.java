package com.imjcker.manager.charge.config;

import com.imjcker.manager.charge.cron.bill.service.*;
import com.imjcker.manager.charge.cron.bill.config.CompareFlinkAndRedisJobProperties;
import com.imjcker.manager.charge.cron.bill.config.CustomerBalanceJobProperties;
import com.imjcker.manager.charge.cron.bill.config.CustomerBillJobProperties;
import com.imjcker.manager.charge.cron.bill.config.DatasourceBillJobProperties;
import com.imjcker.manager.charge.cron.bill.service.*;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author kjy 2020-03-06
 **/
@Slf4j
@EnableScheduling
@Configuration
@EnableConfigurationProperties({
        CustomerBillJobProperties.class,
        DatasourceBillJobProperties.class,
        CustomerBalanceJobProperties.class,
        CompareFlinkAndRedisJobProperties.class
})
public class GlobalQuartzConfig {
    private final CustomerBillJobProperties customerBillJobProperties;
    private final DatasourceBillJobProperties datasourceBillJobProperties;
    private final CustomerBalanceJobProperties customerBalanceJobProperties;
    private final CompareFlinkAndRedisJobProperties compareFlinkAndRedisJobProperties;

    @Autowired
    public GlobalQuartzConfig(CustomerBillJobProperties customerBillJobProperties, DatasourceBillJobProperties datasourceBillJobProperties,
                              CustomerBalanceJobProperties customerBalanceJobProperties,
                              CompareFlinkAndRedisJobProperties compareFlinkAndRedisJobProperties) {
        this.customerBillJobProperties = customerBillJobProperties;
        this.datasourceBillJobProperties = datasourceBillJobProperties;
        this.customerBalanceJobProperties = customerBalanceJobProperties;
        this.compareFlinkAndRedisJobProperties = compareFlinkAndRedisJobProperties;
    }

    // 每天五点对账flink余额任务
    @Bean
    public JobDetail compareFlinkAndRedisJobDetail() {
        return JobBuilder.newJob(CompareFlinkAndRedisJob.class)
                .withIdentity(compareFlinkAndRedisJobProperties.getTopic())
                .build();
    }

    @Bean
    public Trigger compareFlinkAndRedisTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(compareFlinkAndRedisJobProperties.getTopic())
                .withSchedule(CronScheduleBuilder.cronSchedule(compareFlinkAndRedisJobProperties.getCron()))
                .startNow()
                .build();
    }

    // 客户每日redis余额更新任务
    @Bean
    public JobDetail customerBalanceJobDetail() {
        return JobBuilder.newJob(CustomerBalanceJob.class)
                .withIdentity(customerBalanceJobProperties.getTopic())
                .build();
    }

    @Bean
    public Trigger customerBalanceTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(customerBalanceJobProperties.getTopic())
                .withSchedule(CronScheduleBuilder.cronSchedule(customerBalanceJobProperties.getCron()))
                .startNow()
                .build();
    }

    // 客户账单任务
    @Bean
    public JobDetail customerBillDayJobDetail() {
        return JobBuilder.newJob(CustomerBillDayJob.class)
                .withIdentity(customerBillJobProperties.getDay())
                .build();
    }

    @Bean
    public Trigger customerBillDayTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(customerBillJobProperties.getDay())
                .withSchedule(CronScheduleBuilder.cronSchedule(customerBillJobProperties.getCron4day()))
                .startNow()
                .build();
    }

    @Bean
    public JobDetail customerBillMonthJobDetail() {
        return JobBuilder.newJob(CustomerBillMonthJob.class)
                .withIdentity(customerBillJobProperties.getMonth())
                .build();
    }

    @Bean
    public Trigger customerBillMonthTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(customerBillJobProperties.getMonth())
                .withSchedule(CronScheduleBuilder.cronSchedule(customerBillJobProperties.getCron4month()))
                .startNow()
                .build();
    }

    @Bean
    public JobDetail customerBillYearJobDetail() {
        return JobBuilder.newJob(CustomerBillYearJob.class)
                .withIdentity(customerBillJobProperties.getYear())
                .build();
    }

    @Bean
    public Trigger customerBillYearTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(customerBillJobProperties.getYear())
                .withSchedule(CronScheduleBuilder.cronSchedule(customerBillJobProperties.getCron4year()))
                .startNow()
                .build();
    }

    // 数据源账单定时任务
    @Bean
    public JobDetail datasourceBillDayJobDetail() {
        return JobBuilder.newJob(DatasourceBillDayJob.class)
                .withIdentity(datasourceBillJobProperties.getDay())
                .build();
    }

    @Bean
    public Trigger datasourceBillDayTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(datasourceBillJobProperties.getDay())
                .withSchedule(CronScheduleBuilder.cronSchedule(datasourceBillJobProperties.getCron4day()))
                .startNow()
                .build();
    }

    @Bean
    public JobDetail datasourceBillMonthJobDetail() {
        return JobBuilder.newJob(DatasourceBillMonthJob.class)
                .withIdentity(datasourceBillJobProperties.getMonth())
                .build();
    }

    @Bean
    public Trigger datasourceBillMonthTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(datasourceBillJobProperties.getMonth())
                .withSchedule(CronScheduleBuilder.cronSchedule(datasourceBillJobProperties.getCron4month()))
                .startNow()
                .build();
    }

    @Bean
    public JobDetail datasourceBillYearJobDetail() {
        return JobBuilder.newJob(DatasourceBillYearJob.class)
                .withIdentity(datasourceBillJobProperties.getYear())
                .build();
    }

    @Bean
    public Trigger datasourceBillYearTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(datasourceBillJobProperties.getYear())
                .withSchedule(CronScheduleBuilder.cronSchedule(datasourceBillJobProperties.getCron4year()))
                .startNow()
                .build();
    }
}
