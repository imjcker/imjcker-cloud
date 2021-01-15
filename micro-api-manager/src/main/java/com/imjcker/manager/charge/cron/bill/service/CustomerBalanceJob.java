package com.imjcker.manager.charge.cron.bill.service;

import com.imjcker.manager.charge.service.CustomerBalanceJobService;
import com.imjcker.manager.charge.service.CustomerBalanceJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author WT
 * @Date 10:19 2020/4/14
 * @Version CustomerBalanceJob v1.0
 * @Desicrption
 */
@Slf4j
@Component
public class CustomerBalanceJob implements Job {

    @Autowired
    private CustomerBalanceJobService customerBalanceJobService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("开始每日redis余额更新...");
        customerBalanceJobService.updateRedisBalancePerDay();
    }
}
