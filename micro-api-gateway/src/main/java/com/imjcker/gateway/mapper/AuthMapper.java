package com.imjcker.gateway.mapper;

import com.imjcker.gateway.po.CompanyAppsAuth;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface AuthMapper {

    /**  zuul使用查询company_apps_auth */
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "apiId", column = "api_id"),
            @Result(property = "appKey", column = "app_key"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "status", column = "status"),
            @Result(property = "strategyUuid", column = "strategy_uuid"),
            @Result(property = "stock", column = "stock"),
            @Result(property = "price", column = "price")
    })
    @Select("select * from company_apps_auth where app_key =#{appKey} and api_id = #{apiId} and status =1 ")
    List<CompanyAppsAuth> findAppKeyAndApiId(@Param("appKey") String ipAddress, @Param("apiId") Integer apiId);

}
