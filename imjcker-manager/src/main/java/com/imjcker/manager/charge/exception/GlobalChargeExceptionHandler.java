package com.imjcker.manager.charge.exception;

import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@ControllerAdvice
public class GlobalChargeExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalChargeExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public CommonResult handler(Exception ex) {
        logger.error("发生异常",ex);
        return CommonResult.ex(ResultStatusEnum.ERROR.getCode(), ex.getMessage(), null);
    }

    @ExceptionHandler(value = DataValidationException.class)
    @ResponseBody
    public CommonResult dataValidationExceptionHandler(HttpServletRequest req, DataValidationException ex) {
        logger.warn("全局异常处理器捕获到数据校验异常: {}", ex.getMessage());
        CommonResult commonResult = new CommonResult(ResultStatusEnum.WARN, null);
        commonResult.setMessage(ex.getMessage());
        return commonResult;
    }
}
