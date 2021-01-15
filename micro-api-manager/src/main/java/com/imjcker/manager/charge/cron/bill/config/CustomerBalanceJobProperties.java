package com.imjcker.manager.charge.cron.bill.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author WT
 * @Date 9:13 2020/4/13
 * @Version CustomerBalanceJobProperties v1.0
 * @Desicrption 客户余额每天更新当日余额
 */
@Data
@ConfigurationProperties("customer-balance-job")
public class CustomerBalanceJobProperties {
    private String topic = "每日零点更新客户当日余额";
    private String cron = "0 0 0 * * ? *";
}
