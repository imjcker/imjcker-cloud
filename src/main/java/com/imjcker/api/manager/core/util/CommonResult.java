package com.imjcker.api.manager.core.util;

import lombok.Data;

@Data
public class CommonResult<T> {
    private int code;
    private String message;
    private T data;

    public CommonResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResult(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

}
