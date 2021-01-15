package com.imjcker.gateway.model;

import lombok.Builder;
import lombok.Data;

/**
 * 存储下游请求信息到ES，用于记录请求情况
 */
@Data
@Builder
public class QueryInfo  {
    protected Integer apiId;
    protected String apiName;
    protected String uid;
    //计费字段
    private String chargeUuid;//计费规则uuid
    private double price;//单价
    private Boolean chargeFlag;//是否计费（实际情况下根据查询计费或者查得计费得到的，需要zuul中判定请求计费时为true）

    private  String appKey;
    private  String url;
    private  long createTime;
    private  String reqParam;
    private  String result;
    private  long spendTime;
    private  Integer responseCode;
    private String ipv4;
}
