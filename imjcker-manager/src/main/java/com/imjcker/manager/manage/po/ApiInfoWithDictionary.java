package com.imjcker.manager.manage.po;

import java.util.List;

/**
 * .
 * User: lxl
 * Date: 2017/9/22
 * Time: 12:31
 * Description: 用于流量控制功能主页面查询
 */
public class ApiInfoWithDictionary {

    private Integer id;

    private String apiName;

    private String interfaceName;

    private String callBackSuccessExample;

    private String responseTransParam;

    List<ApiRateDistributeWithDictionary> subApis;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getCallBackSuccessExample() {
        return callBackSuccessExample;
    }

    public void setCallBackSuccessExample(String callBackSuccessExample) {
        this.callBackSuccessExample = callBackSuccessExample;
    }

    public String getResponseTransParam() {
        return responseTransParam;
    }

    public void setResponseTransParam(String responseTransParam) {
        this.responseTransParam = responseTransParam;
    }

    public List<ApiRateDistributeWithDictionary> getSubApis() {
        return subApis;
    }

    public void setSubApis(List<ApiRateDistributeWithDictionary> subApis) {
        this.subApis = subApis;
    }
}
