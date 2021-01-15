package com.imjcker.api.common.exception.vo;

public class MyCompanyRechargeException extends RuntimeException{

	private String message;

    public MyCompanyRechargeException(String message) {
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
