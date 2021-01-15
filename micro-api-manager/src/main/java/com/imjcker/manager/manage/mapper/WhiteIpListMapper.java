package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.model.PageInfo;
import com.lemon.common.vo.WhiteIpList;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WhiteIpListMapper {

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appKeyId", column = "app_key_id"),
            @Result(property = "appKey", column = "app_key"),
            @Result(property = "ipAddress", column = "ip_address"),
            @Result(property = "description", column = "description"),
            @Result(property = "status_flag", column = "status_flag"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select({"<script>",
            "select c.id,c.ip_address,c.description,c.create_time,c.update_time,c.app_key_id,a.app_key from company_white_ip c left join company_apps a on c.app_key_id=a.id ",
            "WHERE",
            "<if test='ipAddress != null'>",
            "c.ip_address like CONCAT('%','${ipAddress}','%')  AND ",
            "</if>",
            "<if test='appKeyId != null'>",
            "c.app_key_id = #{appKeyId}  AND ",
            "</if>",
            "c.status_flag =1  ORDER BY update_time,create_time desc ",
            "limit ${(currentPage-1)*pageSize},#{pageSize}",
            "</script>"})
    List<WhiteIpList> findByIpAddress(@Param("ipAddress") String ipAddress, @Param("appKeyId") Integer appKeyId, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appKeyId", column = "app_key_id"),
            @Result(property = "ipAddress", column = "ip_address"),
            @Result(property = "description", column = "description"),
            @Result(property = "status_flag", column = "status_flag"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("select * from company_white_ip where ip_address like concat('%',#{ipAddress},'%') and status_flag =1 ")
    List<WhiteIpList> findIpAddress(@Param("ipAddress") String ipAddress);


    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appKeyId", column = "app_key_id"),
            @Result(property = "appKey", column = "app_key"),
            @Result(property = "ipAddress", column = "ip_address"),
            @Result(property = "description", column = "description"),
            @Result(property = "status_flag", column = "status_flag"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("select *  from company_white_ip WHERE status_flag =1")
    List<WhiteIpList> findAll();


    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appKeyId", column = "app_key_id"),
            @Result(property = "ipAddress", column = "ip_address"),
            @Result(property = "description", column = "description"),
            @Result(property = "status_flag", column = "status_flag"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("select * from company_white_ip where id=#{id} and status_flag =1 ")
    WhiteIpList findIpAddressById(@Param("id") Integer id);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appKeyId", column = "app_key_id"),
            @Result(property = "ipAddress", column = "ip_address"),
            @Result(property = "description", column = "description"),
            @Result(property = "status_flag", column = "status_flag"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("select * from company_white_ip where ip_address like concat('%',#{ipAddress},'%') and id !=#{id} and status_flag=1")
    List<WhiteIpList> findIpAddressIgnoreSelf(@Param("ipAddress") String ipAddress, @Param("id") Integer id);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appKeyId", column = "app_key_id"),
            @Result(property = "appKey", column = "app_key"),
            @Result(property = "ipAddress", column = "ip_address"),
            @Result(property = "description", column = "description"),
            @Result(property = "status_flag", column = "status_flag"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
//    @Select("select * from company_white_ip where status_flag=1 ORDER BY update_time,create_time desc limit ${(currentPage-1)*pageSize},#{pageSize} ")
    @Select("select c.id,c.ip_address,c.description,c.create_time,c.update_time,c.app_key_id,a.app_key from company_white_ip c left join company_apps a on c.app_key_id=a.id where c.status_flag=1 ORDER BY update_time,create_time desc limit ${(currentPage-1)*pageSize},#{pageSize} ")
    List<WhiteIpList> findByPage(PageInfo pageInfo);


    @Select("select count(1) from company_white_ip where status_flag=1")
    Integer getTotalCount();

    //    @Select("select count(1) from company_white_ip where ip_address like concat('%',#{ipAddress},'%') and status_flag=1")
    @Select({"<script>",
            "select count(1) from company_white_ip c left join company_apps a on c.app_key_id=a.id ",
            "WHERE",
            "<if test='ipAddress != null'>",
            "c.ip_address like CONCAT('%','${ipAddress}','%')  AND ",
            "</if>",
            "<if test='appKeyId != null'>",
            "c.app_key_id = #{appKeyId}  AND ",
            "</if>",
            "c.status_flag =1",
            "</script>"})
    Integer getTotalCountByIpAddress(@Param("ipAddress") String ipAddress, @Param("appKeyId") Integer appKeyId);

    @Insert("insert into company_white_ip (ip_address,app_key_id,description,status_flag,create_time,update_time) values (#{ipAddress},#{appKeyId},#{description},#{status_flag},#{createTime},#{updateTime})")
    Integer insert(WhiteIpList whiteIpList);

    //    @Delete("delete from company_white_ip where id=#{id}")
    @Update("update company_white_ip set status_flag=0 where id=#{id}")
    Integer deleteById(Integer id);

    @Update("update company_white_ip set ip_address=#{ipAddress},app_key_id=#{appKeyId},description=#{description},update_time=#{updateTime} where id = #{id}")
    Integer update(WhiteIpList whiteIpList);
}
