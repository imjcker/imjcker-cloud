package com.imjcker.domain;

import lombok.Data;

@Data
public final class JsonResult<T> {
    private String code;

    private String message;

    private T data;

    private JsonResult() {
        this.code = "200";
        this.message = "success";
        this.data = null;
    }

    private JsonResult(T data) {
        this.code = "200";
        this.message = "success";
        this.data = data;
    }

    private JsonResult(T data, String message) {
        this.code = "200";
        this.message = message;
        this.data = data;
    }

    public static JsonResult success() {
        return new JsonResult();
    }

    public static JsonResult success(Object data) {
        return new JsonResult(data);
    }

    public static JsonResult success(Object data, String message) {
        return new JsonResult(data, message);
    }

    public static JsonResult fail(String message) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode("500");
        jsonResult.setMessage(message);
        jsonResult.setData(null);
        return jsonResult;
    }
}
