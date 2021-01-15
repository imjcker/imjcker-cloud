package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.App;
import com.imjcker.manager.manage.po.Strategy;
import com.imjcker.manager.manage.po.query.AppQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AppOldMapper extends BaseMapper<App> {
    @Select({"<script>",
            "select count(1) from app_certification ",
            "<where>",
            "<if test = 'query.appId != null'>",
            " id = #{query.appId} AND",
            "</if>",
            "<if test = 'query.appName != null'>",
            "appName like CONCAT('%','${query.appName}','%')  AND",
            "</if>",
            " status =1 ",
            "</where>",
            "</script>"})
    public int queryCount(@Param("query") AppQuery query);

    @Select({"<script>",
            "select * from app_certification ",
            "<where>",
            "<if test = 'query.appId != null'>",
            " id = #{query.appId} AND",
            "</if>",
            "<if test = 'query.appName != null'>",
            "appName like CONCAT('%','${query.appName}','%')  AND",
            "</if>",
            " status =1 ORDER BY createTime desc",
            "</where>",
            " limit #{query.startIndex}, #{query.pageSize}",
            "</script>"})
    public List<App> queryByPage(@Param("query") AppQuery query);

    @Select({"<script>",
            "select count(1) from app_certification ",
            "<where>",
            "status =1",
            "and limitStrategyuuid =#{limitStrategyuuid}",
            "</where>",
            "</script>"})
    public int queryCountByStrategy(@Param("query") AppQuery query,@Param("limitStrategyuuid")String limitStrategyuuid);

    @Select({"<script>",
            "select * from app_certification ",
           "<where>",
            "status =1",
            "and limitStrategyuuid =#{limitStrategyuuid}",
            "</where>",
            " limit #{query.startIndex}, #{query.pageSize}",
            "</script>"})
    public List<App> queryPageByStrategy(@Param("query") AppQuery query,@Param("limitStrategyuuid")String limitStrategyuuid);
    @Update("update app_certification set limitStrategyuuid =null where id=#{id}")
    void setNullUuid(@Param("id") Integer appId);
    @Select("select count(1) from app_certification A, current_limit_strategy B " +
            "where A.status=1 and B.status=1 and A.limitStrategyuuid=B.uuid and B.uuid =#{limitUid} and A.id=#{appId}")
    int checkAccredit(@Param("limitUid") String limitStrategyUid, @Param("appId") Integer appId);
    @Update("update app_certification set status =#{status} where id=#{id}")
    void updateStatus(@Param("id")Integer appId, @Param("status")Integer disenable);
    @Update("update app_certification set appName=#{entity.appName},description=#{entity.description},updateTime=#{entity.updateTime} where id =#{entity.id}")
    void updateApp(@Param("entity")App entity);
    @Select("select * from current_limit_strategy where uuid =#{uuid} and status =1")
    Strategy selectStrategyByUuid(@Param("uuid") String limitStrategyuuid );
    @Update("update app_certification set limitStrategyuuid=#{entity.limitStrategyuuid},updateTime=#{entity.updateTime} where id =#{entity.id}")
    void updateUuid(@Param("entity")App entity);
    @Select({"<script>",
            "select count(1) from app_certification A,api_app_relation B",
            "<where>",
            "A.status =1 and B.status=1 and A.id=B.appCertificationId",
            "and B.apiId=#{apiId}",
            "</where>",
            "</script>"})
     int queryCountByApiId(@Param("query") AppQuery query,@Param("apiId")Integer apiId);

    @Select({"<script>",
            "select * from app_certification A,api_app_relation B",
            "<where>",
            "A.status =1 and B.status=1 and A.id=B.appCertificationId",
            "and B.apiId=#{apiId}",
            "</where>",
            " limit #{query.startIndex}, #{query.pageSize}",
            "</script>"})
    List<App> queryPageByApiId(@Param("query") AppQuery query,@Param("apiId")Integer apiId);
    @Select({"<script>",
            "select * from app_certification ",
            "<where>",
            "<if test = 'appName != null'>",
            "appName like CONCAT('%','${appName}','%')  AND ",
            "</if>",
            " status =1 ORDER BY createTime desc",
            "</where>",
            "</script>"})
    List<App> queryApps(@Param("appName")String appName);
    @Select({"<script>",
            "select count(1) from app_certification ",
            "<where>",
            "status =1",
            "and appName =#{appName}",
            "</where>",
            "</script>"})
    int countName(@Param("appName") String name);
}
