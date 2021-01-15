package com.imjcker.manager.scheduled.service.impl;

import com.lemon.common.exception.vo.BusinessException;
import com.imjcker.manager.scheduled.dto.JobStdByVo;
import com.imjcker.manager.scheduled.dto.QuartzJobsVo;
import com.imjcker.manager.scheduled.job.JobAware;
import com.imjcker.manager.scheduled.listener.SchedulerListener;
import com.imjcker.manager.scheduled.service.ScheduledService;
import com.imjcker.manager.scheduled.util.SpringBeanTypeUtils;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author WT
 * @Date 14:23 2019/8/7
 * @Desicrption 动态定时任务统一调度
 */
@Service
public class ScheduledServiceImpl implements ScheduledService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledServiceImpl.class);

    @Autowired
    private Scheduler scheduler;

    private JobListener scheduleListener;

    /**
     * 获取所有可以启动定时任务以及当前状态 (即所有的自定义 Job实现类)
     * @return
     */
    @Override
    public List<JobStdByVo> getJobStdBy() {
        List<JobStdByVo> list = new ArrayList<>();
        Map<String, JobAware> jobAwareMap = SpringBeanTypeUtils.getBeanMap(JobAware.class);
        // 获取全部Job 的全限定名及任务主题
        Map<String, String> jobMap = new HashMap<>();
        for (JobAware jobAware : jobAwareMap.values()) {
            String packageName = jobAware.getClass().getName();
            if (packageName.contains("$")) {
                jobMap.put(jobAware.jobTopic(), packageName.substring(0,packageName.indexOf("$")));
            }else {
                jobMap.put(jobAware.jobTopic(), packageName);
            }

        }
        // 获取正在执行定时任务
        List<QuartzJobsVo> jobOnlines = this.findAllJob();
        for (Map.Entry<String, String> entry : jobMap.entrySet()) {
            String topic = entry.getKey();
            String className = entry.getValue();
            JobStdByVo vo = new JobStdByVo();
            vo.setJobTopic(topic);
            vo.setClassName(className);
            // 匹配正在进行的任务
            jobOnlines.stream()
                    .filter(job ->
                            Objects.equals(job.getClassName(), className))
                    .findAny()
                    .ifPresent(vo::setQuartzJobsVo);

            list.add(vo);
        }
        return list;
    }

    /**
     * 开始定时任务
     * @param cron
     * @param jobName
     * @param jobGroup
     * @param jobClass
     */
    @Override
    public void startJob(String cron, String jobName, String jobGroup,
                         Class<? extends Job> jobClass) {
        try {
            if (scheduleListener == null) {
                scheduleListener = new SchedulerListener();
                scheduler.getListenerManager().addJobListener(scheduleListener);
            }
            // 检查此 jobClass 是否已在任务中
            if (checkJobOnline(jobClass)) {
                // 存在
                throw new BusinessException("已存在此任务,请检查后重试");
            }
            JobKey jobKey = new JobKey(jobName, jobGroup);
            if (scheduler.checkExists(jobKey)) {
                throw new BusinessException("任务名和任务分组名已存在,请更换后再试");
            }

            scheduleJob(cron, scheduler, jobName, jobGroup, jobClass);
        } catch (SchedulerException e) {
            throw new BusinessException("开启定时任务失败");
        }
    }

    /**
     * 删除定时任务
     * @param jobName
     * @param jobGroup
     */
    @Override
    public void deleteJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new BusinessException("删除定时任务失败");
        }
    }

    /**
     * 暂停定时任务
     * @param jobName
     * @param jobGroup
     */
    @Override
    public void pauseJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            throw new BusinessException("暂停定时任务失败");
        }
    }

    /**
     * 恢复定时任务
     * @param jobName
     * @param jobGroup
     */
    @Override
    public void resumeJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            throw new BusinessException("恢复定时任务失败");
        }
    }

    /**
     * 清空所有当前scheduler 对象下的定时任务 ,全局目前就一个 scheduler对象
     */
    @Override
    public void clearAll() {
        try {
            scheduler.clear();
        } catch (SchedulerException e) {
            throw new BusinessException("清空定时任务失败");
        }
    }

    /**
     * 获取所有正在执行(包括暂停)的任务
     * @return
     */
    @Override
    public List<QuartzJobsVo> findAllJob() {
        List<QuartzJobsVo> list = new ArrayList<>();
        // 获取分组名
        try {
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for (String triggerGroupName : triggerGroupNames) {
                GroupMatcher<TriggerKey> matcher = GroupMatcher.groupEquals(triggerGroupName);

                // 获取分组下的触发器
                Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(matcher);
                for (TriggerKey triggerKey : triggerKeys) {
                    // 根据触发器获取任务,获取详细信息
                    CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    JobKey jobKey = cronTrigger.getJobKey();
                    JobDetailImpl jobDetail = (JobDetailImpl) scheduler.getJobDetail(jobKey);
                    // 获取任务类名
                    String className = jobDetail.getJobClass().getName();
                    // 获取任务状态
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);

                    String state = triggerState.name();
                    QuartzJobsVo vo = new QuartzJobsVo();
                    vo.setJobDetailName(jobDetail.getName());
                    vo.setGroupName(triggerGroupName);
                    vo.setJobCronExpression(cronTrigger.getCronExpression());
                    vo.setTimeZone(cronTrigger.getTimeZone().getID());
                    vo.setState(state);
                    vo.setClassName(className);
                    list.add(vo);
                }
            }
        } catch (SchedulerException e) {
            throw new BusinessException("获取定时任务失败");
        }
        return list;
    }

    /**
     * 动态创建任务
     * @param cron
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @param jobClass
     */
    private void scheduleJob(String cron, Scheduler scheduler, String jobName,
                             String jobGroup, Class<? extends Job> jobClass) {
        /**
         *  可以先将任务从数据库中提取,或者更新
         */
        // 如果没引入数据库,直接新建任务,初始化参数
        try {
            JobDetail jobDetail = JobBuilder
                    .newJob(jobClass)
                    .withIdentity(jobName, jobGroup)
                    .build();
            // 设置cron表达式
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger cronTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobName, jobGroup)
                    .withSchedule(cronScheduleBuilder)
                    .build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (SchedulerException e) {
            throw new BusinessException("创建定时任务失败");
        }
    }

    /**
     * 检查该任务类是否已经在运行
     * @param jobClass
     * @return
     */
    private boolean checkJobOnline(Class<? extends Job> jobClass) {
        // 获得正在运行的任务
        List<QuartzJobsVo> jobOnlines = this.findAllJob();
        return jobOnlines.stream()
                .anyMatch(vo ->
                        Objects.equals(jobClass.getName(), vo.getClassName()));
    }
}
