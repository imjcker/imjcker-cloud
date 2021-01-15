package com.imjcker.api.handler.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imjcker.api.common.vo.ZuulHeader;
import com.imjcker.api.handler.util.AsyncCollectionUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Configuration
public class LogAop {

    private static final Logger log = LoggerFactory.getLogger(LogAop.class);

    private static final String MDC_UID = "uid";

    @Pointcut("execution(* com.lemon.client.controller..*.*(..))")
    public void serviceLog() {

    }

    @Before(value = "serviceLog())")
    public void doBefore(JoinPoint joinPoint) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String uid = request.getHeader(ZuulHeader.PARAM_KEY_ORDER_ID);

        MDC.put(MDC_UID, uid);
        log.info("==========开始第三方接口调用=========");
    }

    @AfterReturning(pointcut = "serviceLog()", returning = "returnValue")
    public void doAfter(JoinPoint joinPoint, Object returnValue) {

        AsyncCollectionUtil.removeCharge();
        AsyncCollectionUtil.removeAsync();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("#@begin=SUCCESS;retMsg:{};Server;remoteHost:{};localHost:{};",returnValue, request.getRemoteHost(),request.getLocalAddr());
        log.info("==========结束第三方接口调用=========");
        MDC.clear();
    }

    @Around("serviceLog()")
    public Object timeAround(ProceedingJoinPoint joinPoint) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        Object obj = null;
        try {
            obj = joinPoint.proceed();
        } catch (Throwable throwable) {

            log.error("LogAop error: {}", throwable.getMessage());
        }
        long endTime = System.currentTimeMillis();
        log.info("调用总共花费时间 : {} (ms)", endTime - startTime);
        return obj;
    }
}
