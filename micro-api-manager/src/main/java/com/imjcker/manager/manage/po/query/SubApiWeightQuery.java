package com.imjcker.manager.manage.po.query;

/**
 * .
 * User: lxl
 * Date: 2017/9/22
 * Time: 10:14
 * Description:
 */
public class SubApiWeightQuery {

    //上级api Id
    private Integer apiId;
    //下级api Id
    private Integer subId;
    //权重
    private Integer weight;
    //接口名字 唯一
    private String intefaceName;

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getIntefaceName() {
        return intefaceName;
    }

    public void setIntefaceName(String intefaceName) {
        this.intefaceName = intefaceName;
    }

    public static SubApiWeightQuery of(Integer apiId,Integer subAppId,Integer weight,String interfaceName){
        SubApiWeightQuery subApiWeightQuery =  new SubApiWeightQuery();
        subApiWeightQuery.setApiId(apiId);
        subApiWeightQuery.setSubId(subAppId);
        subApiWeightQuery.setWeight(weight);
        subApiWeightQuery.setIntefaceName(interfaceName);
        subApiWeightQuery.setWeight(weight);
        return subApiWeightQuery;
    }
}
