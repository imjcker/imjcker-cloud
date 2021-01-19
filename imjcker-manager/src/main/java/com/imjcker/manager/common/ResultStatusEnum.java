package com.imjcker.manager.common;

/**
 * <p>Title: ResultStatus.java
 * <p>Description: 通用数据访问对象的业务状态枚举类
 * <p>Copyright: Copyright © 2017, Lemon, All Rights Reserved.
 *
 * @author Lemon.zl
 * @version 1.0
 */
public enum ResultStatusEnum {

    SUCCESS(2000, "Success"),
    WARN(3000, "Warn"),
    NOT_FOUND(4000, "Not Found"),
    ERROR(5000, "Error"),
    AGENCY_EXIT(5000, "Error"),
    PARAMS_INPUT_NULL(5000, "Error"),
    AGENCY_SOURCE(1, "1");

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
