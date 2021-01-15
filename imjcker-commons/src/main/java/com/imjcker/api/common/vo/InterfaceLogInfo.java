package com.imjcker.api.common.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author qiuwen
 * @Date 17:21 2020/4/14
 * @Desicrption
 */
@Data
@Builder
public class InterfaceLogInfo {
    /**
     * 接口id
     */
    private Integer apiId;
    /**
     * 计费类型
     */
    private Integer billingType;
    /**
     * 计费周期
     */
    private Integer billingCycle;
    /**
     * 最大调用量
     */
    private Long billingCycleLimit;

    /**  是否计费 */
    private Boolean chargeFlag;
}
