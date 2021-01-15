package com.imjcker.api.handler.exception;

/**
 *  外部数据源异常(包括清洗服务)
 *  @author lihongjie
 */
public class ThirdBusinessException extends RuntimeException {

    public ThirdBusinessException() {

    }

    public ThirdBusinessException(String message) {
        super(message);
    }

    public ThirdBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThirdBusinessException(Throwable cause) {
        super(cause);
    }
}
