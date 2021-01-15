package com.imjcker.manager.scheduled.dto;

/**
 * @Author WT
 * @Date 16:54 2019/8/7
 * @Desicrption
 */
public class JobStdByVo {

    private String jobTopic;
    private String className;
    private QuartzJobsVo quartzJobsVo;

    public String getJobTopic() {
        return jobTopic;
    }

    public JobStdByVo setJobTopic(String jobTopic) {
        this.jobTopic = jobTopic;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public JobStdByVo setClassName(String className) {
        this.className = className;
        return this;
    }

    public QuartzJobsVo getQuartzJobsVo() {
        return quartzJobsVo;
    }

    public JobStdByVo setQuartzJobsVo(QuartzJobsVo quartzJobsVo) {
        this.quartzJobsVo = quartzJobsVo;
        return this;
    }
}
