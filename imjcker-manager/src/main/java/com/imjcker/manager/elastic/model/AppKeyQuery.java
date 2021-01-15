package com.imjcker.manager.elastic.model;
import lombok.Data;

/**
 * 存储日志数据到mongo，用于记录请求情况
 */
@Data
public class AppKeyQuery {
    private int apiId;
    private String apiName;
    private String uid;
    private  String appKey;
    private  long startTime;
    private  long endTime;
    //分页
    private int pageNum;
    private int pageSize;
}
