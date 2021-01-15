package com.imjcker.api.common.vo;

/**
 * <p>Title: ResultStatus.java
 * <p>Description: 通用数据访问对象的业务状态枚举类
 * <p>Copyright: Copyright © 2017, Lemon, All Rights Reserved.
 *
 * @author Lemon.zl
 * @version 1.0
 */
public enum ResultStatusEnum {
    TIMEOUT(500, "timeout"),
    SUCCESS(2000, "Success"),
    MOCK(2001, "MOCK"),
    WARN(3000, "Warn"),
    USER_ERROR(3010,"账号或密码错误"),
    PASSWORD_ERROR(3020,"原密码错误"),
    PASSWORD_INVALID(3030,"密码必须包含大小写字母,数字,特殊符号"),
    NAME_INVALID(3040,"名称支持汉字、英文字母、数字、英文格式的下划线，必须以英文字母或汉字开头，4~50个字符"),
    NOT_FOUND(4000, "Not Found"),
    NO_ERROR(4050, "次数不能超过一千万"),
    NAME_REPEAT(4060,"名称已存在，请修改名称"),
    PATH_REPEAT(4061,"path已存在,请修改path"),
    HAS_TASK(4070,"要添加的数据已存在"),
    AGENCY_SOURCE(4071,"该分组为源接口，不允许配置接口账号信息"),
    AGENCY_EXITE(4072,"该机构下已存在相同配置,请选择编辑"),
    PARAM_NULL(4080,"请求参数不合规"),
    PARAM_ERROR(4090,"更新参数不合规"),
    UNIQUE_ERROR_NAME(4092,"Name 唯一性校验失败"),
    UNIQUE_ERROR_PATH(4093,"Path 唯一性校验失败"),
    TIME_ERROR(4081,"所选时间段不满足查询条件"),
    ERROR(5000, "Error"),
    DOWNLOAD_API_ERROR(5010,"API文档不存在"),
    TOWN_ACCOUNT_NOT_FOUND(5050,"无可用帐号，联系开发人员"),
    API_NULL(4081,"API为空"),
    TFB_SUCCESS(200, "ok"),
    // 500 作为风控转码后的数据异常码
//    TFB_INTERNAL_ERROR(500, "Server Internal Error"),
    TFB_INTERNAL_TIMEOUT(550, "服务器内部服务调用超时"),
    TFB_INTERNAL_REFUSED(551, "服务器内部服务调用拒绝"),
    TFB_INTERNAL_ERROR(553, "Server Internal Error"),
    TFB_DATASOURCE_EXCEPTION(552, "外部数据源调用异常"),
    TFB_MAX_LIMIT_EXCEPTION(1001, "超过最大限流" ),
    TFB_API_HTTPPATH_NOT_FOUND(1005, "接口路径校验不存在"),
    TFB_API_HTTPMETHOD_NOT_SUPPORT(1006, "http method请求方式不一致"),
    TFB_API_APPKEY_AUTH_FAILED(1007, "appKey校验失败"),
    TFB_API_APPKEY_TIMEOUT(1014, "appKey校验超时"),
    TFB_API_WHITELIST_NOT_FOUND(1008, "ip白名单不存在"),
    TFB_API_WHITELIST_TIMEOUT(1012, "ip白名单校验超时"),
    TFB_API_IP_ERROR(1009, "获取IP发生错误"),
    TFB_API_PARAMETERS_ERROR(1010, "请求参数错误"),
    TFB_API_BALANCE_ERROR(1017, "余额不足"),

    PARAMS_INPUT_NULL(4082,"请求参数为空"),
    PARAMS_OUTPUT_NULL(4083,"外部数据源返回数据为空"),
    RATE_LIMIT_TOTAL_APPKEY(1011, "APPKEY总量限流警告"),
    RATE_LIMIT_TOTAL_API(1012, "API总量限流警告"),
    RATE_LIMIT_TOTAL_APPKEY_API(1013, "APPKEY_API总量限流警告"),
    RATE_LIMIT_RATE_APPKEY(1014, "APPKEY频率限流警告"),
    RATE_LIMIT_RATE_API(1015, "API频率限流警告"),
    RATE_LIMIT_RATE_APPKEY_API(1016, "APPKEY_API频率限流警告"),
    RATE_CHECK(1018,"操作警告"),

    SERVICE_ERROR(1100,"系统错误,请联系管理员"),
    SIGN_FAIL(1001,"当前参数不支持验签"),

    CLEAN_AND_HEALTH(1999, "success"),
    //异步推送实时返回结果标记
    TFB_ASYNCHRONOUS_PUSH_SUCCESS(3344, "asynchronous request succeed.");






    private final int code;
    private final String message;

    ResultStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
