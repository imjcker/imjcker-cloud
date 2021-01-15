package com.imjcker.api.common.exception.vo;

/**
 * <p>Title: DataValidationException.java
 * <p>Description: 数据校验异常类
 * <p>Copyright: Copyright © 2016, Lemon, All Rights Reserved.
 *
 * @author Lemon.zl
 * @version 1.0
 */
public class DataValidationException extends RuntimeException {

    /** 异常信息 */
    private String message;

    public DataValidationException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
