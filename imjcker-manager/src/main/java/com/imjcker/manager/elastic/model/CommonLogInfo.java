package com.imjcker.manager.elastic.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 上下游数据采集公共字段
 */
@SuperBuilder
@Data
public class CommonLogInfo {
    /**
     接口id
     */
    protected Integer apiId;

    /**
     接口名称
     */
    protected String apiName;
    /**
     流水号
     */
    protected String uid;
    //计费字段
    private String chargeUuid;//计费规则uuid
    private double price;//单价
    private Boolean chargeFlag;//是否计费（实际情况下根据查询计费或者查得计费得到的，需要zuul中判定请求计费时为true）
    public CommonLogInfo(){}

}
