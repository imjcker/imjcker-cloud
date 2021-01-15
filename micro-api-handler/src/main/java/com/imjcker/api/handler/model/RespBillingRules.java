package com.imjcker.api.handler.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author qiuwen
 * @Date 16:51 2020/3/31
 * @Desicrption
 */
@Data
public class RespBillingRules implements Serializable {
    private static final long serialVersionUID = 3060368011070635883L;

    /**
    主键id
     */
    private Integer id;
    /**
    规则唯一uid
     */
    private String uuid;
    /**
    规则名称
     */
    private String name;
    /**
    计费类型
     */
    private Integer billingType;
    /**
    计费周期 1-年，2-季度，3-月
     */
    private Integer billingCycle;
    /**
    周期内最大次数
     */
    private Long billingCycleLimit;
    /**
    计费模式
     */
    private Integer billingMode;
    /**
     * 规则创建时间
     */
    private Long createTime;
    /**
    规则更新时间
     */
    private Long updateTime;
    /**
    规则是否可用，1可用，0不可用
     */
    private Integer status;
}
