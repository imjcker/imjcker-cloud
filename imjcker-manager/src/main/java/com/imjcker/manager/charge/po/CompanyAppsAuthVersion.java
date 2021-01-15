package com.imjcker.manager.charge.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
@Data
@Table(name = "company_apps_auth_version")
public class CompanyAppsAuthVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="",name="id")
    private Long id;

    @Column(name = "api_id")
    @ApiModelProperty(value="",name="apiId")
    private Integer apiId;

    @Column(name = "app_key")
    @ApiModelProperty(value="",name="appKey")
    private String appKey;

    @Column(name = "start_time")
    @ApiModelProperty(value="",name="startTime")
    private Long startTime;

    @Column(name = "end_time")
    @ApiModelProperty(value="",name="endTime")
    private Long endTime;

    @Column(name = "create_time")
    @ApiModelProperty(value="",name="createTime")
    private Long createTime;

    @Column(name = "update_time")
    @ApiModelProperty(value="",name="updateTime")
    private Long updateTime;

    @Column(name = "strategy_uuid")
    @ApiModelProperty(value="",name="strategyUuid")
    private String strategyUuid;

    @ApiModelProperty(value="",name="price")
    private BigDecimal price;
}
