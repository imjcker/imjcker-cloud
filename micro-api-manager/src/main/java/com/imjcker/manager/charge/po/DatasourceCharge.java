package com.imjcker.manager.charge.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
@Data
@Table(name = "datasource_charge")
public class DatasourceCharge implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id",name="id")
    private Integer id;

    /**
     * 数据源ID
     */
    @Column(name = "group_id")
    @ApiModelProperty(value="数据源ID",name="groupId")
    private Integer groupId;

    /**
     * 接口ID
     */
    @Column(name = "api_id")
    @ApiModelProperty(value="接口ID",name="apiId")
    private Integer apiId;

    /**
     * 计费规则ID
     */
    @Column(name = "billing_rules_uuid")
    @ApiModelProperty(value="计费规则ID",name="billingRulesUuid")
    private String billingRulesUuid;

    /**
     * 单价
     */
    @ApiModelProperty(value="单价",name="price")
    private BigDecimal price;

    /**
     * 创建时间
     */
    @Column(name = "start_time")
    @ApiModelProperty(value="创建时间",name="startTime")
    private Long startTime;

    /**
     * 更新时间
     */
    @Column(name = "end_time")
    @ApiModelProperty(value="更新时间",name="endTime")
    private Long endTime;

    /**
     * 状态,0:可用, 1:不可用
     */
    @ApiModelProperty(value="状态,0:可用, 1:不可用",name="status")
    private Integer status;

    /**
     * 总量
     */
    @ApiModelProperty(value="总量",name="stock")
    private Integer stock;
}
