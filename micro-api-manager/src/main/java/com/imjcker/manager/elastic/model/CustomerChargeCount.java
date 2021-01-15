package com.imjcker.manager.elastic.model;
import lombok.Data;

import java.util.List;

@Data
public class CustomerChargeCount {
    /*
    客户标识
     */
    private String appKey;
    /*
    调用量
     */
    private long count;
    /*
     * 详情list
     */
    private List<ApiChargeCount> customerApiChargelistCount;
}
