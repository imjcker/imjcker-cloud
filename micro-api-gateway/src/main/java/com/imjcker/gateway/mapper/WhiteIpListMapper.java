package com.imjcker.gateway.mapper;

import com.github.pagehelper.PageInfo;
import com.imjcker.api.common.vo.WhiteIpList;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WhiteIpListMapper {

    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "ipAddress",column = "ip_address"),
            @Result(property = "description",column = "description"),
            @Result(property = "status_flag",column = "status_flag"),
            @Result(property = "createTime",column = "create_time"),
            @Result(property = "updateTime",column = "update_time")
    })
    @Select("select * from company_white_ip where ip_address like concat('%',#{ipAddress},'%') and status_flag=1 ORDER BY update_time,create_time desc limit ${(currentPage-1)*pageSize},#{pageSize} ")
    List<WhiteIpList> findByIpAddress(@Param("ipAddress") String ipAddress, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);

    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "ipAddress",column = "ip_address"),
            @Result(property = "description",column = "description"),
            @Result(property = "status_flag",column = "status_flag"),
            @Result(property = "createTime",column = "create_time"),
            @Result(property = "updateTime",column = "update_time")
    })
    @Select("select * from company_white_ip where ip_address like concat('%',#{ipAddress},'%') and status_flag =1 ")
    List<WhiteIpList> findIpAddress(@Param("ipAddress") String ipAddress);

    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "ipAddress",column = "ip_address"),
            @Result(property = "description",column = "description"),
            @Result(property = "status_flag",column = "status_flag"),
            @Result(property = "createTime",column = "create_time"),
            @Result(property = "updateTime",column = "update_time")
    })
    @Select("select * from company_white_ip where ip_address like concat('%',#{ipAddress},'%') and id !=#{id} and status_flag=1")
    List<WhiteIpList> findIpAddressIgnoreSelf(@Param("ipAddress") String ipAddress, @Param("id") Integer id);

    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "ipAddress",column = "ip_address"),
            @Result(property = "description",column = "description"),
            @Result(property = "status_flag",column = "status_flag"),
            @Result(property = "createTime",column = "create_time"),
            @Result(property = "updateTime",column = "update_time")
    })
    @Select("select * from company_white_ip where status_flag=1 and status_flag=1 ORDER BY update_time,create_time desc limit ${(currentPage-1)*pageSize},#{pageSize} ")
    List<WhiteIpList> findByPage(PageInfo pageInfo);


    @Select("select count(1) from company_white_ip where status_flag=1")
    Integer getTotalCount();

    @Select("select count(1) from company_white_ip where ip_address like concat('%',#{ipAddress},'%') and status_flag=1")
    Integer getTotalCountByIpAddress(@Param("ipAddress") String ipAddress);

    @Insert("insert into company_white_ip (ip_address,description,status_flag,create_time,update_time) values (#{ipAddress},#{description},#{status_flag},#{createTime},#{updateTime})")
    void insert(WhiteIpList whiteIpList);

//    @Delete("delete from company_white_ip where id=#{id}")
    @Update("update company_white_ip set status_flag=0 where id=#{id}")
    void deleteById(Integer id);

    @Update("update company_white_ip set ip_address=#{ipAddress},description=#{description},update_time=#{updateTime} where id = #{id}")
    void update(WhiteIpList whiteIpList);
}
