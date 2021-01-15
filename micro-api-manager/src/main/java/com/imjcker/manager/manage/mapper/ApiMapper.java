package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.Api;
import com.imjcker.manager.manage.po.query.ApiQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ApiMapper extends BaseMapper<Api> {
    @Select("select groupName from api_group where id= #{api.apiGroupId} and status=1")
     String findGroupName(@Param("api") Api api);

    @Select("select count(1) from api_info A,api_app_relation B " +
            "where A.id=B.apiId and B.appCertificationId=#{query.appId} and A.status=1 and B.status=1 ")
    int queryCountByApp(@Param("query") ApiQuery query);
   @Select("select A.*,B.env,B.createTime as accreditTime,B.id as apiAppId from api_info A,api_app_relation B " +
            "where A.id=B.apiId and B.appCertificationId=#{query.appId} and A.status=1 and B.status=1 " +
            "limit #{query.startIndex}, #{query.pageSize}")
    List<Api> queryPageByApp(@Param("query") ApiQuery query);

    @Select({"<script>",
            "select count(1) from api_info ",
            "<where>",
            " status =1 ",
            "</where>",
            "</script>"})
    public int queryCount(@Param("query")ApiQuery query);

    @Select({"<script>",
            "select * from api_info ",
            "<where>",
            " status =1 ORDER BY createTime desc",
            "</where>",
            " limit #{query.startIndex}, #{query.pageSize}",
            "</script>"})
    public List<Api> queryByPage(@Param("query") ApiQuery query);

    @Select({"<script>",
            "select * from api_info ",
            "<where>",
            "<if test = 'apiName != null'>",
            "apiName like CONCAT('%','${apiName}','%')  AND ",
            "</if>",
            " status =1 ORDER BY createTime desc",
            "</where>",
            "</script>"})
    public List<Api> queryApis(@Param("apiName")String apiName);

}
