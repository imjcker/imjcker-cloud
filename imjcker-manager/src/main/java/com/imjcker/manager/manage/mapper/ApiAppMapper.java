package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiFindApp;
import com.imjcker.manager.manage.po.query.ApiAppQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ApiAppMapper extends BaseMapper<ApiFindApp> {
    @Select({"<script>",
            "select count(1) from api_app_relation ",
            "</script>"})
    public int queryCount(@Param("query") ApiAppQuery query);

    @Select({"<script>",
            "select * from api_app_relation ",
            " limit #{query.startIndex}, #{query.pageSize}",
            "</script>"})
    public List<ApiFindApp> queryByPage(@Param("query") ApiAppQuery query);
    @Select({"<script>",
            "select count(1) from api_app_relation ",
            "where apiId=#{apiId} and env =#{env} and appCertificationId =#{appId} and status=1" ,
            "</script>"})
    public int checkAccredit(@Param("env") Integer env, @Param("apiId") Integer apiId, @Param("appId") Integer appId);

    @Update("update api_app_relation set status =#{status},updateTime=#{updateTime} where appCertificationId=#{appId}")
    void updateStatusByAppID(@Param("appId")Integer appId, @Param("status") Integer status,@Param("updateTime")Long currentTime);
    @Update("update api_app_relation set status =#{status},updateTime=#{updateTime} where apiId=#{entity.apiId} and appCertificationId=#{entity.appCertificationId} and env=#{entity.env}")
    void updateStatus(@Param("entity")ApiFindApp entity, @Param("status")Integer status,@Param("updateTime")Long currentTime);
    @Select("select count(1) from api_app_relation where status =1 and apiId=#{entity.apiId} and appCertificationId=#{entity.appCertificationId} and env=#{entity.env}")
    int countApiFindApp(@Param("entity")ApiFindApp record);
}
