package com.imjcker.gateway.po;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author WT
 * @Date 9:43 2020/2/27
 * @Version CompanyApps v1.0
 * @Desicrption
 */
@Data
public class CompanyApps implements Serializable {
    private Long id;
    private String appKey;
    private String appName;
    private Long createTime;
    private Long updateTime;
    private Integer statusFlag;
    private String description;
    private BigDecimal balance;
    private String strategyUuid;
    private Integer stock;
    private BigDecimal price;
    private String privateKey;
    private String publicKey;

    @Override
    public String toString() {
        return "CompanyApps{" +
                "id=" + id +
                ", appKey='" + appKey + '\'' +
                ", appName='" + appName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", statusFlag=" + statusFlag +
                ", description='" + description + '\'' +
                ", balance=" + balance +
                ", strategyUuid='" + strategyUuid + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                ", privateKey=" + privateKey +
                ", publicKey=" + publicKey +
                '}';
    }
}
