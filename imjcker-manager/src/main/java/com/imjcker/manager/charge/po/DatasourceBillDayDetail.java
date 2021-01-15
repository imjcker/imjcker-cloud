package com.imjcker.manager.charge.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
@Data
@Table(name = "datasource_bill_day_detail")
public class DatasourceBillDayDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="",name="id")
    private Integer id;

    /**
     * 数据源标识
     */
    @Column(name = "group_name")
    @ApiModelProperty(value="数据源标识",name="groupName")
    private String groupName;

    /**
     * 接口id
     */
    @Column(name = "api_id")
    @ApiModelProperty(value="接口id",name="apiId")
    private Integer apiId;

    /**
     * 规则uuid
     */
    @Column(name = "billing_rules_uuid")
    @ApiModelProperty(value="规则uuid",name="billingRulesUuid")
    private String billingRulesUuid;

    /**
     * 单价
     */
    @ApiModelProperty(value="单价",name="price")
    private BigDecimal price;

    /**
     * 调用量
     */
    @ApiModelProperty(value="调用量",name="count")
    private Long count;

    /**
     * 总额，时间计费规则不计算总额
     */
    @ApiModelProperty(value="总额，时间计费规则不计算总额",name="amount")
    private BigDecimal amount;

    @Column(name = "create_time")
    @ApiModelProperty(value="",name="createTime")
    private Long createTime;
}
