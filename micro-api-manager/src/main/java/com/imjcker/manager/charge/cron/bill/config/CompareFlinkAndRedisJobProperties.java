package com.imjcker.manager.charge.cron.bill.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author WT
 * @Date 17:55 2020/4/26
 * @Version CompareFlinkAndRedisJobProperties v1.0
 * @Desicrption
 */
@Data
@ConfigurationProperties("compare-balance-job")
public class CompareFlinkAndRedisJobProperties {
    private String topic = "每日五点对账客户/合约当日余额";
    private String cron = "0 0 5 * * ? *";
}
