package com.imjcker.api.common.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 存储下游请求信息到ES，用于记录请求情况
 */
@SuperBuilder
@Data
public class QueryInfo extends CommonLogInfo  {
    private  String appKey;
    private  String url;
    private  long createTime;
    private  String reqParam;
    private  String result;
    private  long spendTime;
    private  Integer responseCode;
}
