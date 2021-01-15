package com.imjcker.gateway.mapper;

import com.imjcker.gateway.po.CompanyApps;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author WT
 * @Date 11:17 2020/2/24
 * @Version CustomerMapper v1.0
 * @Desicrption
 */
@Mapper
public interface CustomerMapper {

    CompanyApps findCustomerByAppKey(@Param("appKey") String appKey);
}
