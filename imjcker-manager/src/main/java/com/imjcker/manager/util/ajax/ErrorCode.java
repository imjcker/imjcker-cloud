package com.imjcker.manager.util.ajax;
/**
 * 定义系统错误代码
 * 定义规则：
 *     主代码定义规则：模块名称_MAIN_错误类型名称（可选)
 *     子代码定义规则：模块名称_SUB_具体错误类型名称
 */
public final class ErrorCode {

    /////////////////////////////////////////////定义系统级别错误/////////////////////////////////////////////////////////

    /**拦截器处理时发生系统错误*/
    public static int INTERCEPTOR_MAIN_DOERROR = 10;
    /**前端请求必要参数为空的异常代码*/
    public static int CONTROLLER_MAIN_REQUEST_PARAM_NULL = 11;


    /**
     * AC错误模块
     */
    public static class ACErrorNum{
        /**定义AC认证的错误主代码*/
        public static int AC_MAIN = 100;
        /**定义没有登录的代码*/
        public static int AC_SUB_NO_LOGIN = 100100;
        /**定义登录过期的代码*/
        public static int AC_SUB_LOGIN_EXPIRES = 100101;
        /**身份验证出现系统错误*/
        public static int AC_SUB_SYS_ERROR = 100102;
    }
}
