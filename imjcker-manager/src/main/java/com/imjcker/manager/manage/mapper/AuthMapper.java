package com.imjcker.manager.manage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AuthMapper {

//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "apiId", column = "apiId"),
//            @Result(property = "appKey", column = "appKey"),
//            @Result(property = "createTime", column = "createTime"),
//            @Result(property = "updateTime", column = "updateTime"),
//            @Result(property = "startTime", column = "startTime"),
//            @Result(property = "endTime", column = "endTime"),
//            @Result(property = "status", column = "status")
//    })
//    @Select("select * from company_apps_auth where appKey like concat('%',#{appKey},'%') and apiId=#{apiId} and  status_flag=1 ORDER BY updateTime,createTime desc limit ${(currentPage-1)*pageSize},#{pageSize} ")

//    @Select({"<script>",
//            "select * from company_apps_auth ",
//            "WHERE",
//            "<if test='appKey != null'>",
//            "appKey like CONCAT('%','${appKey}','%')  AND",
//            "</if>",
//            "<if test='apiId != null'>",
//            "apiId =#{apiId}  AND",
//            "</if>",
//            "status =1  ORDER BY updateTime,createTime desc",
//            " limit ${(currentPage-1)*pageSize},#{pageSize}",
//            "</script>"})
//    List<CompanyAppsAuth> findByKeyword(@Param("appKey") String appKey, @Param("apiId") Integer apiId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);

//    @Select({"<script>",
//            "select c.id,c.appKey,c.apiId,a.apiName,c.startTime,c.endTime,c.createTime,c.updateTime from company_apps_auth c left join api_info a on c.apiId=a.id",
//            "WHERE",
//            "<if test='appKey != null'>",
//            "c.appKey like CONCAT('%','${appKey}','%')  AND",
//            "</if>",
//            "<if test='apiId != null'>",
//            "c.apiId =#{apiId}  AND",
//            "</if>",
//            " c.status=1 and a.status=1 ORDER BY c.updateTime,c.createTime desc",
//            " limit ${(currentPage-1)*pageSize},#{pageSize}",
//            "</script>"})
//    List<CompanyAppsAuth> findByKeyword(@Param("appKey") String appKey, @Param("apiId") Integer apiId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);
//
//
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "apiId", column = "apiId"),
//            @Result(property = "appKey", column = "appKey"),
//            @Result(property = "createTime", column = "createTime"),
//            @Result(property = "updateTime", column = "updateTime"),
//            @Result(property = "startTime", column = "startTime"),
//            @Result(property = "endTime", column = "endTime"),
//            @Result(property = "status", column = "status")
//    })
////    @Select("select * from company_apps_auth where appKey =#{appKey} and apiId = #{apiId} and status =1 ")
////    List<CompanyAppsAuth> findAppKeyAndApiId(@Param("appKey") String ipAddress,@Param("apiId")Integer apiId);
//
//    @Select({"<script>",
//            "select * from company_apps_auth ",
//            "WHERE",
//            "<if test='appKey != null'>",
//            "appKey =#{appKey}  AND",
//            "</if>",
//            "<if test='apiId != null'>",
//            "apiId =#{apiId}  AND",
//            "</if>",
//            "<if test='id != null'>",
//            "id =#{id}  AND",
//            "</if>",
//            " status=1",
//            "</script>"})
//    List<CompanyAppsAuth> findAppKeyAndApiId(@Param("appKey") String ipAddress,@Param("apiId")Integer apiId,@Param("id")Integer id);
//
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "apiId", column = "apiId"),
//            @Result(property = "appKey", column = "appKey"),
//            @Result(property = "createTime", column = "createTime"),
//            @Result(property = "updateTime", column = "updateTime"),
//            @Result(property = "startTime", column = "startTime"),
//            @Result(property = "endTime", column = "endTime"),
//            @Result(property = "status", column = "status")
//    })
////    @Select("select * from company_apps_auth where status=1  ORDER BY updateTime,createTime desc limit ${(currentPage-1)*pageSize},#{pageSize} ")
////    List<CompanyAppsAuth> findByPage(PageInfo pageInfo);
//
//    @Select("select c.id,c.appKey,c.apiId,a.apiName,c.startTime,c.endTime,c.createTime,c.updateTime from company_apps_auth c left join api_info a on c.apiId=a.id where c.status=1 and a.status=1 ORDER BY c.updateTime,c.createTime desc limit ${(currentPage-1)*pageSize},#{pageSize} ")
//    List<CompanyAppsAuth> findByPage(PageInfo pageInfo);
//
//    @Select("select count(1) from company_apps_auth where status=1")
//    Integer getTotalCount();
//
//    @Select({"<script>",
//            "select count(1) from company_apps_auth ",
//            "WHERE",
//            "<if test='appKey != null'>",
//            "appKey like CONCAT('%','${appKey}','%')  AND",
//            "</if>",
//            "<if test='apiId != null'>",
//            "apiId = #{apiId} AND",
//            "</if>",
//            "status =1 ",
//            "</script>"})
//    Integer getTotalCountByKeyword(@Param("appKey") String appKey, @Param("apiId") Integer apiId);
//
//    @Insert("insert into company_apps_auth (apiId,appKey,startTime,endTime,createTime,updateTime,status) values (#{apiId},#{appKey},#{startTime},#{endTime},#{createTime},#{updateTime},#{status})")
//    void insert(CompanyAppsAuth appKey);
//
//    @Update("update company_apps_auth set status=0 where id=#{id}")
//    void deleteById(Integer id);
//
//    @Update("update company_apps_auth set startTime=#{startTime},endTime=#{endTime},updateTime=#{updateTime} where id = #{id}")
//    void update(CompanyAppsAuth appKey);
//
    @Update("update company_apps_auth set status=0 where api_id=#{apiId}")
    void deleteByApiId(Integer apiId);


}
