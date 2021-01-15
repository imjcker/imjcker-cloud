package com.imjcker.api.handler.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.vo.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/LogLevel")
public class LogLevelChangeController {
    private static String root = "root";

    /**
     * 修改日志级别
     * allLevel：全局日志修改
     * level & path：path路径下修改日志级别
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/changeLevel", produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public String changeLevel(@RequestBody JSONObject jsonObject) {
        String allLevel = jsonObject.getString("allLevel");
        String level = jsonObject.getString("level");
        String path = jsonObject.getString("path");
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        if (!StringUtils.isEmpty(allLevel)) {
            ch.qos.logback.classic.Logger logger = loggerContext.getLogger(root);
            logger.setLevel(Level.toLevel(allLevel));
        }
        if (!StringUtils.isEmpty(level) && !StringUtils.isEmpty(path)) {
            ch.qos.logback.classic.Logger vLogger = loggerContext.getLogger(path);
            if (vLogger != null)
                vLogger.setLevel(Level.toLevel(level));
        }
        return JSON.toJSONString(CommonResult.success());
    }

    /**
     * 查看日志级别
     * 当path为空时，查看全局日志级别；
     * 当path不为空时，查看path下的日志级别
     *
     * @param jsonObject
     * @return查看
     */
    @PostMapping(value = "/showLevel", produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public String showLevel(@RequestBody JSONObject jsonObject) {
        String path = jsonObject.getString("path");
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        if (StringUtils.isEmpty(path))
            path = root;
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger(path);
        String level = logger.getLevel().levelStr;
        return JSON.toJSONString(CommonResult.success(level));
    }
}
