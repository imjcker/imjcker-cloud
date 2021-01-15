package com.imjcker.manager.manage.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author ztzh_tanhh 2010/01/06
 * 定时清理临时文件任务
 **/
@Slf4j
@Component
public class FileCleanupJob implements Job {

    @Value("${docs.temporary-path}")
    private String temporaryPath;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            File dir = new File(temporaryPath);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (Exception e) {
                                throw new Exception("删除目录[" + file.getAbsolutePath() + "]失败");
                            }
                        } else {
                            if (!file.delete()) {
                                log.error("删除文件[{}]失败", file.getAbsolutePath());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("清理临时文件任务异常：{}", e.getMessage());
        }
    }
}
