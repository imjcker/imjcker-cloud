package com.imjcker.manager.scheduled.service;

import com.imjcker.manager.scheduled.dto.JobStdByVo;
import com.imjcker.manager.scheduled.dto.QuartzJobsVo;
import org.quartz.Job;

import java.util.List;

/**
 * @Author WT
 * @Date 14:23 2019/8/7
 * @Desicrption
 */
public interface ScheduledService {

    void startJob(String cron, String jobName, String jobGroup,
                  Class<? extends Job> jobClass);

    void deleteJob(String jobName, String jobGroup);

    void pauseJob(String jobName, String jobGroup);

    void resumeJob(String jobName, String jobGroup);

    void clearAll();

    List<QuartzJobsVo> findAllJob();

    List<JobStdByVo> getJobStdBy();
}
