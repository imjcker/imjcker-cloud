package com.imjcker.manager.charge.cron.bill.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author thh 2020-03-19
 **/
@Data
@ConfigurationProperties("datasource-bill-job")
public class DatasourceBillJobProperties {
    private String day = "datasourceBillDayService";
    private String cron4day = "0 0 1 * * ?";// 每天凌晨1点执行一次

    private String month = "datasourceBillMonthService";
    private String cron4month = "0 0 1 1 * ?";// 每月1号凌晨1点执行一次

    private String year = "datasourceBillYearService";
    private String cron4year = "0 0 1 1 1 ?";// 每年1月1号凌晨1点执行一次
}
