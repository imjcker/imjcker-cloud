package com.imjcker.manager.elastic.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerApiChargeCount {
    /*
    接口id
     */
    private Integer apiId;
    /*
    计费规则uuid
     */
    private String chargeUuid;
    /*
    合约单价
     */
    private BigDecimal price;
    /*
    调用量
     */
    private long count;
}
