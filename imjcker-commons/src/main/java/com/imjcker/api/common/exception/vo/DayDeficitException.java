package com.imjcker.api.common.exception.vo;

/**
 * <p>Title:
 * <p>Description: 计算数据源亏损情况：每天即将亏损
 * <p>Copyright: Copyright © 2016, Lemon, All Rights Reserved.
 *
 * @author Lemon.zl
 * @version 1.0
 */
public class DayDeficitException extends RuntimeException {

    /** 异常信息 */
    private String message;

    public DayDeficitException(String message) {
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
