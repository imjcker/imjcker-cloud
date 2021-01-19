package com.imjcker.manager.config;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * @author yezhiyuan
 * @version V1.0
 * @Title: 全局日志处理
 * @Package com.lemon.gateway.aop
 * @date 2017年4月6日 下午2:30:13
 */
@Aspect
@Component
@Slf4j
public class GlobalLogAspect {
    private static ThreadLocal<MyLogger> loger = new ThreadLocal<>();


    @Pointcut("execution(public * com.imjcker.manager.*.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String targetMethod = StringUtils.join(joinPoint.getSignature().getDeclaringTypeName(), ".", joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        String params = Arrays.toString(args);
        //将参数中的HttpServletResponse对象去掉不做打印
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpServletResponse) {
                Object[] objects = Arrays.copyOf(args, args.length);
                objects[i] = "HttpServletResponse对象";
                params = Arrays.toString(objects);
                break;
            }
        }
        MyLogger myLogger = MyLogger.builder().uri(uri).method(method).targetMethod(targetMethod).params(params).build();
        loger.set(myLogger);
//        WebLogUtils.begin(uri, method, targetMethod, params);
    }

    @AfterReturning(returning = "a", pointcut = "webLog()")
    public void doAfterReturning(Object a) throws Throwable {
        MyLogger myLogger = loger.get();
        myLogger.setResult(a.toString());
        log.info("请求信息:\r\nuri: {}\r\nmethod: {}\r\ntargetMethod: {}\r\nparams: {}\r\nresult: {}",
                myLogger.uri,
                myLogger.method,
                myLogger.targetMethod,
                myLogger.params,
                myLogger.result);
    }

    @AfterThrowing(throwing = "a", pointcut = "webLog()")
    public void doRecoveryActions(Throwable a) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        a.printStackTrace(pw);
    }

    @Data
    @Builder
    public static class MyLogger{
        private String uri;
        private String method;
        private String targetMethod;
        private String params;
        private String result;
    }
}
