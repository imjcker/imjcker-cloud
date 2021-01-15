package com.imjcker.manager.charge.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ztzh_tanhh 2020/3/18
 **/
@Configuration
@MapperScan(basePackages = {"com.imjcker.manager.charge.mapper"})
public class MybatisConfig {
}
