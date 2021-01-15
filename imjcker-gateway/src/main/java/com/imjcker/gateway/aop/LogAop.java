package com.imjcker.gateway.aop;

import com.imjcker.api.common.vo.ZuulHeader;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAop {
    private static final String MDC_UID = "uid";
    @Pointcut("execution(* com.imjcker.gateway.filter..*.*(..))")
    public void serviceLog() {
    }

    @Before(value = "serviceLog())")
    public void doBefore() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String uid = (String) request.getAttribute(ZuulHeader.PARAM_KEY_ORDER_ID);
        MDC.put(MDC_UID, uid);
    }

}
