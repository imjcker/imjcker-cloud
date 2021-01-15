package com.imjcker.manager.manage.po;

import java.util.List;

/**
 * .
 * User: lxl
 * Date: 2017/9/22
 * Time: 12:31
 * Description: 用于流量控制功能主页面查询
 */
public class ApiInfoWithSubApi{

    private Integer id;

    private String apiName;

    private String groupId;

    private String apiGroupName;

    private String interfaceName;

    private Integer weight;

    List<ApiRateDistribute> subApis;

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

    public String getApiGroupName() {
        return apiGroupName;
    }

    public void setApiGroupName(String apiGroupName) {
        this.apiGroupName = apiGroupName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<ApiRateDistribute> getSubApis() {
        return subApis;
    }

    public void setSubApis(List<ApiRateDistribute> subApis) {
        this.subApis = subApis;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
