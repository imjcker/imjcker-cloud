package com.imjcker.gateway.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author WT
 * @Date 10:27 2020/2/27
 * @Version CompanyAppsAuth v1.0
 * @Desicrption
 */
@ApiModel
@Data
@Table(name = "company_apps_auth")
public class CompanyAppsAuth implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="自增 id",name="id")
    private Integer id;

    /**
     * 接口id
     */
    @Column(name = "api_id")
    @ApiModelProperty(value="接口id",name="apiId")
    private Integer apiId;

    /**
     * 客户标识
     */
    @Column(name = "app_key")
    @ApiModelProperty(value="客户标识",name="appKey")
    private String appKey;

    /**
     * 接口调用有效起始时间
     */
    @Column(name = "start_time")
    @ApiModelProperty(value="接口调用有效起始时间",name="startTime")
    private Long startTime;

    /**
     * 接口调用终止时间
     */
    @Column(name = "end_time")
    @ApiModelProperty(value="接口调用终止时间",name="endTime")
    private Long endTime;

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

    /**
     * 计费策略uuid
     */
    @Column(name = "strategy_uuid")
    @ApiModelProperty(value="计费策略uuid",name="strategyUuid")
    private String strategyUuid;

    /**
     * 调用余量
     */
    @ApiModelProperty(value="调用余量",name="stock")
    private Long stock;

    /**
     * 价格,按条计费时为单价，包时计费为总价格
     */
    @ApiModelProperty(value="价格,按条计费时为单价，包时计费为总价格",name="price")
    private BigDecimal price;
}
