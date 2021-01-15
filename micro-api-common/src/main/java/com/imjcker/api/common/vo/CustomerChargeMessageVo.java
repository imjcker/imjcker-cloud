package com.imjcker.api.common.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author WT
 * @Date 16:23 2020/4/15
 * @Version CustomerChargeMessageVo v1.0
 * @Desicrption  客户充值, 数据请求, 变更计费规则,发送消息到flink
 */
@Data
@Builder
public class CustomerChargeMessageVo {

    private String appKey;

    private Integer apiId;

    private Integer messageMode;

    private String type;

    private String result;

    private BigDecimal money;

    private Integer count;

    private Boolean isDeleteCustomerBalance;

    private Boolean isDeleteCustomerCount;

    private Boolean isDeleteAuthCount;
}
