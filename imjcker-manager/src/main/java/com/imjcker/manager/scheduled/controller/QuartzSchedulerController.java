package com.imjcker.manager.scheduled.controller;

import com.imjcker.manager.scheduled.dto.JobStdByVo;
import com.imjcker.manager.scheduled.dto.QuartzJobsVo;
import com.imjcker.manager.scheduled.service.ScheduledService;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author WT
 * @Date 17:04 2019/8/6
 * @Desicrption 任务调度控制层
 */
@RestController
@RequestMapping("/quartz")
public class QuartzSchedulerController {

    @Autowired
    private ScheduledService scheduledService;

    /**
     * 获取所有可执行的任务及当前状态
     * @return
     */
    @GetMapping("/getJobStdBy")
    public CommonResult getJobStdBy() {
        List<JobStdByVo> list = scheduledService.getJobStdBy();
        return new CommonResult(ResultStatusEnum.SUCCESS, list);
    }

    /**
     * 开启定时任务,根据 job 包下的类名,开启任务
     * @param cron
     * @param jobName
     * @param jobGroup
     * @param className
     * @return
     * @throws ClassNotFoundException
     */
    @PostMapping("/startTask")
    public CommonResult startJob(@RequestParam String cron,@RequestParam String jobName,
                                 @RequestParam String jobGroup,@RequestParam String className) throws ClassNotFoundException {

        Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(className);
        scheduledService.startJob(cron, jobName,jobGroup, jobClass);
        return new CommonResult(ResultStatusEnum.SUCCESS, "启动任务成功");
    }

    /**
     * 删除定时任务
     * @param jobName
     * @param jobGroup
     * @return
     */
    @PostMapping("/deleteTask")
    public CommonResult deleteJob(@RequestParam String jobName, @RequestParam String jobGroup) {
        scheduledService.deleteJob(jobName,jobGroup);
        return new CommonResult(ResultStatusEnum.SUCCESS, "删除定时任务成功");
    }

    /**
     * 暂停定时任务
     * @param jobName
     * @param jobGroup
     * @return
     */
    @PostMapping("/pauseTask")
    public CommonResult pauseJob(@RequestParam String jobName, @RequestParam String jobGroup) {
        scheduledService.pauseJob(jobName,jobGroup);
        return new CommonResult(ResultStatusEnum.SUCCESS, "暂停定时任务成功");
    }

    /**
     * 恢复定时任务
     * @param jobName
     * @param jobGroup
     * @return
     */
    @PostMapping("/resumeTask")
    public CommonResult resumeJob(@RequestParam String jobName, @RequestParam String jobGroup) {
        scheduledService.resumeJob(jobName,jobGroup);
        return new CommonResult(ResultStatusEnum.SUCCESS, "恢复定时任务成功");
    }

    /**
     * 清空定时任务
     * @return
     */
    @PostMapping("/clearAllTask")
    public CommonResult clearAllJob() {
        scheduledService.clearAll();
        return new CommonResult(ResultStatusEnum.SUCCESS, "清空定时任务成功");
    }

    /**
     * 查看所有在运行(包括暂停)时的定时任务
     * @return
     */
    @GetMapping("/findAllTask")
    public CommonResult findAll() {
        List<QuartzJobsVo> allJob = scheduledService.findAllJob();
        return new CommonResult(ResultStatusEnum.SUCCESS, allJob);
    }

}
