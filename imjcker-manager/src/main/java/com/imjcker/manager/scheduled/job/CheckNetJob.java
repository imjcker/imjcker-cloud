package com.imjcker.manager.scheduled.job;

import com.imjcker.manager.scheduled.service.NetPingService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author WT
 * @Date 10:34 2019/8/6
 * @Desicrption 定时任务访问第三方网络
 */
@Component
public class CheckNetJob implements JobAware {

    private static final Logger logger = LoggerFactory.getLogger(CheckNetJob.class);

    @Autowired
    private NetPingService netPingService;

    @Override
    public String jobTopic() {
        return "数据源网络监控";
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
        logger.info("开始检测第三方数据源。。。");
        netPingService.startPing();
    }

}
