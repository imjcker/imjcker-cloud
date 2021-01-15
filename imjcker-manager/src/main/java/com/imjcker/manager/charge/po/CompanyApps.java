package com.imjcker.manager.charge.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@ApiModel
@Data
@Table(name = "company_apps")
public class CompanyApps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="",name="id")
    private Long id;

    /**
     * appKey机构标识
     */
    @Column(name = "app_key")
    @ApiModelProperty(value="appKey机构标识",name="appKey")
    private String appKey;

    /**
     * app名称
     */
    @Column(name = "app_name")
    @ApiModelProperty(value="app名称",name="appName")
    private String appName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value="创建时间",name="createTime")
    private Long createTime;

    @Column(name = "update_time")
    @ApiModelProperty(value="",name="updateTime")
    private Long updateTime;

    /**
     * 是否启用,1启用,0禁用
     */
    @Column(name = "status_flag")
    @ApiModelProperty(value="是否启用,1启用,0禁用",name="statusFlag")
    private Integer statusFlag;

    /**
     * 描述
     */
    @ApiModelProperty(value="描述",name="description")
    private String description;

    /**
     * 余额
     */
    @ApiModelProperty(value="余额",name="balance")
    private BigDecimal balance;

    /**
     * 客户级别的计费方式
     */
    @Column(name = "strategy_uuid")
    @ApiModelProperty(value="客户级别的计费方式",name="strategyUuid")
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

    /**
     * 公钥
     */
    @Column(name = "public_key")
    @ApiModelProperty(value="公钥",name="publicKey")
    private String publicKey;

    /**
     * 私钥
     */
    @Column(name = "private_key")
    @ApiModelProperty(value="私钥",name="privateKey")
    private String privateKey;
}
