package com.imjcker.manager.exception;

import com.imjcker.manager.common.CommonResult;
import com.imjcker.manager.common.ResultStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>Title: GlobalExceptionHandler.java
 * <p>Description: 全局异常处理器
 * <p>Copyright: Copyright © 2017, Lemon, All Rights Reserved.
 *
 * @author Lemon.kiana
 * @version 1.0
 */
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResult businessExceptionHandler(HttpServletRequest req, BusinessException ex) {
        logger.warn("全局异常处理器捕获到自定义异常.", ex.getMessage());
        CommonResult commonResult = new CommonResult(ResultStatusEnum.WARN, null);
        commonResult.setMessage(ex.getMessage());
        return commonResult;
    }

    @ExceptionHandler(value = DataValidationException.class)
    @ResponseBody
    public CommonResult dataValidationExceptionHandler(HttpServletRequest req, DataValidationException ex) {
        logger.warn("全局异常处理器捕获到数据校验异常: {}", ex.getMessage());
        CommonResult commonResult = new CommonResult(ResultStatusEnum.WARN, null);
        commonResult.setMessage(ex.getMessage());
        return commonResult;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResult defaultHandler(HttpServletRequest req, Exception ex) {
        logger.error("全局异常处理器捕获到未知异常.", ex);
        return CommonResult.ex(ResultStatusEnum.ERROR, ex.getMessage());
    }

}
