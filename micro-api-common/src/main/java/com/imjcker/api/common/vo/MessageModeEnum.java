package com.imjcker.api.common.vo;

/**
 * @Author WT
 * @Date 17:21 2020/4/15
 * @Version MessageModeEnum v1.0
 * @Desicrption
 */
public enum MessageModeEnum {
    CUSTOMER_CHARGE(1,"客户计费"), // 正常数据请求计费
    CUSTOMER_EDIT(2,"客户变更"),   // 客户修改计费规则,合约修改计费规则
    CUSTOMER_INIT(3,"客户初始化");  //客户每天初始化key


    private int code;
    private String message;

    MessageModeEnum(int code, String message) {
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
