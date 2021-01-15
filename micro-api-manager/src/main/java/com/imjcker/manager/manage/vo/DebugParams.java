package com.imjcker.manager.manage.vo;

public class DebugParams {

	private String paramName;

	private Integer paramsType;

	private Integer paramsLocation;

	private Integer paramMust;

	private String model;

	private String paramDescription;

	private String paramsDefaultValue;

	public String getParamDescription() {
		return paramDescription;
	}

	public void setParamDescription(String paramDescription) {
		this.paramDescription = paramDescription;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public Integer getParamsType() {
		return paramsType;
	}

	public void setParamsType(Integer paramsType) {
		this.paramsType = paramsType;
	}

	public Integer getParamsLocation() {
		return paramsLocation;
	}

	public void setParamsLocation(Integer paramsLocation) {
		this.paramsLocation = paramsLocation;
	}
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getParamMust() {
		return paramMust;
	}

	public void setParamMust(Integer paramMust) {
		this.paramMust = paramMust;
	}

	public String getParamsDefaultValue() {
		return paramsDefaultValue;
	}

	public void setParamsDefaultValue(String paramsDefaultValue) {
		this.paramsDefaultValue = paramsDefaultValue;
	}
}
