package com.imjcker.manager.elastic.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 存储下游请求信息到ES，用于记录请求情况
 */
@SuperBuilder
@Data
@NoArgsConstructor
public class QueryInfo extends CommonLogInfo  {
    private  String appKey;
    private  String url;
    private  long createTime;
    private  String reqParam;
    private  String result;
    private  long spendTime;
    private  Integer responseCode;
    private String ipv4;
}
