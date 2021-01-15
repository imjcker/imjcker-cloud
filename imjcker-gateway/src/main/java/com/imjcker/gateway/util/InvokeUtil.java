package com.imjcker.gateway.util;

import javax.servlet.http.HttpServletRequest;

public final class InvokeUtil {

    private InvokeUtil() {

    }

    /**
     *是否为风控，对公业务内部服务调用
     * @param request
     * @return
     */
    public static boolean isInternalInvoke(HttpServletRequest request) {
        return "debugging".equals(request.getHeader("debug"));
    }
}
