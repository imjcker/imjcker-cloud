package com.imjcker.manager.charge.cron.bill.service;

import com.imjcker.manager.charge.service.CompareFlinkAndRedisJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author WT
 * @Date 9:02 2020/4/26
 * @Version CompareFlinkAndRedisJob v1.0
 * @Desicrption  每日凌晨2点对账flink数据
 */

@Slf4j
@Component
public class CompareFlinkAndRedisJob implements Job {

    @Autowired
    private CompareFlinkAndRedisJobService compareFlinkAndRedisJobService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("开始每日与flink对账....");
        compareFlinkAndRedisJobService.reconciliationFlinkBalance();
    }
}
