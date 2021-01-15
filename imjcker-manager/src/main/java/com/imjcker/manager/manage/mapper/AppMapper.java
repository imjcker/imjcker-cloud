package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.model.PageInfo;
import com.lemon.common.vo.CompanyApp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AppMapper {

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appKey", column = "app_key"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "description", column = "description"),
            @Result(property = "statusFlag", column = "status_flag"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("select * from company_apps where app_key like concat('%',#{appKey},'%') and status_flag=1 ORDER BY update_time,create_time desc limit ${(currentPage-1)*pageSize},#{pageSize} ")
    List<CompanyApp> findByAppKey(@Param("appKey") String appKey, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appKey", column = "app_key"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "description", column = "description"),
            @Result(property = "statusFlag", column = "status_flag"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("select * from company_apps where app_key =#{appKey} and status_flag =1 ")
    List<CompanyApp> findAppKey(@Param("appKey") String ipAddress);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appKey", column = "app_key"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "description", column = "description"),
            @Result(property = "statusFlag", column = "status_flag"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
    })
    @Select("select * from company_apps where status_flag =1  order by app_key")
    List<CompanyApp> findAll();


    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appKey", column = "app_key"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "description", column = "description"),
            @Result(property = "statusFlag", column = "status_flag"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("select * from company_apps where status_flag=1  ORDER BY update_time,create_time desc limit ${(currentPage-1)*pageSize},#{pageSize} ")
    List<CompanyApp> findByPage(PageInfo pageInfo);


    @Select("select count(1) from company_apps where status_flag=1")
    Integer getTotalCount();

    @Select("select count(1) from company_apps where app_key like concat('%',#{appKey},'%') and status_flag=1")
    Integer getTotalCountByAppKey(@Param("appKey") String appKey);

    @Insert("insert into company_apps (app_key,app_name,description,status_flag,create_time,update_time) values (#{appKey},#{appName},#{description},#{statusFlag},#{createTime},#{updateTime})")
    void insert(CompanyApp appKey);

    //    @Delete("delete from company_white_ip where id=#{id}")
    @Update("update company_apps set status_flag=0 where id=#{id}")
    void deleteById(Integer id);

    @Update("update company_apps set app_name=#{appName},description=#{description},update_time=#{updateTime} where id = #{id}")
    void update(CompanyApp appKey);

    @Update("update company_apps_auth set status=0 where app_key=#{appKey}")
    void deleteByAppKey(String appKey);

    @Select("select * from company_apps where id=#{id}")
    CompanyApp selectById(Integer id);
}
