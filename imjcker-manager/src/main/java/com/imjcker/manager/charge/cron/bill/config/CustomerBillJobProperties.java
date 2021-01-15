package com.imjcker.manager.charge.cron.bill.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kjy 2020-03-06
 **/
@Data
@ConfigurationProperties("customer-bill-job")
public class CustomerBillJobProperties {
    private String day = "customerBillDayJob";
    private String cron4day = "0 0 2 * * ?";// 每天凌晨1点执行一次

    private String month = "customerBillMonthJob";
    private String cron4month = "0 0 2 1 * ?";// 每月1号凌晨1点执行一次

    private String year = "customerBillYearJob";
    private String cron4year = "0 0 2 1 1 ?";// 每年1月1号凌晨1点执行一次
}
