package com.imjcker.api.handler.po;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RespDatasourceCharge implements Serializable {

    private Integer id;
    /**
     * 分组id
     */
    private Integer groupId;
    /**
     * 接口id
     */
    private Integer apiId;

    /**
     * 计费规则ID
     */
    private String billingRulesUuid;

    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 创建时间
     */
    private Long startTime;

    /**
     * 更新时间
     */
    private Long endTime;
    /**
     * 状态,0:可用, 1:不可用
     */
    private Integer status;
    /**
     * 总量
     */
    private Integer stock;
}
