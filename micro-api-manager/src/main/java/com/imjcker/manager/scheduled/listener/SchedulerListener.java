package com.imjcker.manager.scheduled.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author WT
 * @Date 16:31 2019/8/6
 * @Desicrption
 */
public class SchedulerListener implements JobListener {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerListener.class);

    private static final String LISTENER_NAME = "QuartSchedulerListener";

    @Override
    public String getName() {
        return LISTENER_NAME;
    }

    /**
     * 任务调度前
     * @param context
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        String jobName = context.getJobDetail().getKey().toString();
        logger.info("开始任务调度,任务名称: {}", jobName);
    }

    /**
     * 任务调度被拒绝
     * @param context
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        logger.info("任务调度拒绝: {}",context.getJobDetail().getKey().toString());
    }

    /**
     * 任务调度后
     * @param context
     * @param e
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context,
                               JobExecutionException e) {
        String jobName = context.getJobDetail().getKey().toString();
        logger.info("任务: {} 调度完成", jobName);

        if (e != null && !"".equals(e.getMessage())) {
            logger.info("任务调度出现异常,e: {}", e.getMessage());
        }
    }
}
