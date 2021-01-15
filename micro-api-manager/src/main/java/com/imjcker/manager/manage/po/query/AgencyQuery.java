package com.imjcker.manager.manage.po.query;

import com.imjcker.manager.manage.po.Agency;

public class AgencyQuery extends PageQuery<Agency>{

    /**
    分组号
     */
    private String apiGroupName;
    /**
     *  机构appKey
     */
    private String appKey;
    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 是否源接口
     */
    private Integer sourceFlag;

    public String getApiGroupName() {
        return apiGroupName;
    }

    public void setApiGroupName(String apiGroupName) {
        this.apiGroupName = apiGroupName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public Integer getSourceFlag() {
        return sourceFlag;
    }

    public void setSourceFlag(Integer sourceFlag) {
        this.sourceFlag = sourceFlag;
    }
}
