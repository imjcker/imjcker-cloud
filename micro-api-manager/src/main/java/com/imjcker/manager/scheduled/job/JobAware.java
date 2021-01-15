package com.imjcker.manager.scheduled.job;

import org.quartz.Job;

/**
 * @Author WT
 * @Date 16:22 2019/8/7
 * @Desicrption
 */
public interface JobAware extends Job {

    String jobTopic();
}
