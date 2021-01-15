package com.imjcker.api.handler.model;

public class ThirdResult {

	private Integer errorCode;

	private String errorMsg;

	private String uid;

	private Object data;

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ThirdResult{");
        sb.append("errorCode=").append(errorCode);
        sb.append(", errorMsg='").append(errorMsg).append('\'');
        sb.append(", uid=").append(uid);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }

}
