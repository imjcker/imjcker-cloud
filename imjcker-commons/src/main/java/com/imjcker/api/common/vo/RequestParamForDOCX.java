package com.imjcker.api.common.vo;

public class RequestParamForDOCX {
	private String paramName;
	private String type;
	private String paramsLocation;
	private String paramMust;
	private String paramsDescription;

	public String getParamsLocation() {
		return paramsLocation;
	}
	public void setParamsLocation(String paramsLocation) {
		this.paramsLocation = paramsLocation;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParamMust() {
		return paramMust;
	}
	public void setParamMust(String paramMust) {
		this.paramMust = paramMust;
	}
	public String getParamsDescription() {
		return paramsDescription;
	}
	public void setParamsDescription(String paramsDescription) {
		this.paramsDescription = paramsDescription;
	}


}
