package com.imjcker.manager.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Title: sql工厂类
 * @Package com.lemon.client.config
 * @author yezhiyuan
 * @date 2017年7月11日 上午9:34:56
 * @version V2.0
 */
@SuppressWarnings("unused")
@Configuration
@EnableTransactionManagement
@MapperScan("com.lemon.client.logic.mapper")
public class SqlSessionFactoryConfig /*implements TransactionManagementConfigurer*/ {
/*
    @Autowired
    private DataSource dataSource;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    *//**
     * 创建sqlSessionFactoryBean 实例
     * 并且设置configtion 如驼峰命名.等等
     * 设置mapper 映射路径
     * 设置datasource数据源
     * @return
     * @throws Exception
     *//*
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory createSqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        *//** 设置datasource *//*
        bean.setDataSource(dataSource);
        *//** 设置typeAlias 包扫描路径 *//*
        bean.setTypeAliasesPackage(dataSourceProperties.getTypeAliasPackage());

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources(dataSourceProperties.getMapperLocations()));

        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }*/
}
