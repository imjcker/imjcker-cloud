package com.imjcker.manager.scheduled.dto;

import java.io.Serializable;

/**
 * @Author WT
 * @Date 8:59 2019/8/7
 * @Desicrption
 */
public class QuartzJobsVo implements Serializable {

    private String jobDetailName;
    private String jobCronExpression;
    private String timeZone;
    private String groupName;
    private String state;
    private String className;

    public String getJobDetailName() {
        return jobDetailName;
    }

    public QuartzJobsVo setJobDetailName(String jobDetailName) {
        this.jobDetailName = jobDetailName;
        return this;
    }

    public String getJobCronExpression() {
        return jobCronExpression;
    }

    public QuartzJobsVo setJobCronExpression(String jobCronExpression) {
        this.jobCronExpression = jobCronExpression;
        return this;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public QuartzJobsVo setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public QuartzJobsVo setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public String getState() {
        return state;
    }

    public QuartzJobsVo setState(String state) {
        this.state = state;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public QuartzJobsVo setClassName(String className) {
        this.className = className;
        return this;
    }
}
