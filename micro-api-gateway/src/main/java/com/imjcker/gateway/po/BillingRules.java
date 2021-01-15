package com.imjcker.gateway.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@ApiModel
@Data
@Table(name = "billing_rules")
public class BillingRules implements Serializable {
    private static final long serialVersionUID = 7093340112118552794L;
    /**
     * 自增 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="自增 id",name="id")
    private Integer id;

    /**
     * 规则唯一id
     */
    @ApiModelProperty(value="规则唯一id",name="uuid")
    private String uuid;

    /**
     * 规则名称
     */
    @ApiModelProperty(value="规则名称",name="name")
    private String name;

    /**
     * 计费分类：1-时间计费 2-按条计费
     */
    @Column(name = "billing_type")
    @ApiModelProperty(value="计费分类：1-时间计费 2-按条计费",name="billingType")
    private Integer billingType;

    /**
     * 计费周期(1-按年，2-季度，3-月,-1不限时)
     */
    @Column(name = "billing_cycle")
    @ApiModelProperty(value="计费周期(1-按年，2-季度，3-月,-1不限时)",name="billingCycle")
    private Integer billingCycle;

    /**
     * 时间计费时生效，限定周期内的最大调用量，-1表示不限量
     */
    @Column(name = "billing_cycle_limit")
    @ApiModelProperty(value="时间计费时生效，限定周期内的最大调用量，-1表示不限量",name="billingCycleLimit")
    private Long billingCycleLimit;

    /**
     * 计费模式(默认1查询计费，2查得计费)
     */
    @Column(name = "billing_mode")
    @ApiModelProperty(value="计费模式(默认1查询计费，2查得计费)",name="billingMode")
    private Integer billingMode;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value="创建时间",name="createTime")
    private Long createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @ApiModelProperty(value="更新时间",name="updateTime")
    private Long updateTime;

    /**
     * 状态码,1启用,0禁用
     */
    @ApiModelProperty(value="状态码,1启用,0禁用",name="status")
    private Integer status;
}
