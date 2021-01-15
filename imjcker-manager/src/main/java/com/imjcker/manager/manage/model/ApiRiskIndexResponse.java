package com.imjcker.manager.manage.model;

public class ApiRiskIndexResponse {

    private String title;

    private Object data;

    private boolean success;

    private String message;

    public ApiRiskIndexResponse(boolean success) {
        this.success = success;
    }

    public ApiRiskIndexResponse(boolean success, String message) {
        this.message = message;
        this.success = success;
    }

    public ApiRiskIndexResponse(String title, Object data, boolean success, String message) {
        this.title = title;
        this.data = data;
        this.success = success;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
