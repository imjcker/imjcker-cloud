package com.imjcker.api.common.exception.vo;

/**
 * <p>Title: BusinessException.java
 * <p>Description: 自定义业务异常类
 * <p>Copyright: Copyright © 2016, CQzlll, All Rights Reserved.
 *
 * @author CQzlll.zl
 * @version 1.0
 */
public class BusinessException extends RuntimeException {

    /** 异常信息 */
    private String message;

    public BusinessException(String message) {
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
