package com.imjcker.manager.charge.cron.bill.service;

import com.imjcker.manager.charge.service.DatasourceBillService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author thh 2020-03-19
 **/
@Slf4j
@Component
public class DatasourceBillMonthJob implements Job {

    @Autowired
    private DatasourceBillService datasourceBillService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("开始执行数据源日账单持久化任务");
        datasourceBillService.saveDatasourceBillByMonth(null,null);
    }

}
