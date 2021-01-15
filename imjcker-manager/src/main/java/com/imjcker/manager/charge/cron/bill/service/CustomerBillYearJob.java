package com.imjcker.manager.charge.cron.bill.service;

import com.imjcker.manager.charge.service.CustomerBillService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author kjy 2020-03-06
 **/
@Slf4j
@Component
public class CustomerBillYearJob implements Job {

    @Autowired
    private CustomerBillService customerBillService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("开始执行客户年账单持久化任务");
        customerBillService.saveCustomerChargeByYear(null,null);
    }

}
