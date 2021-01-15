package com.imjcker.manager.charge.mapper;

import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.model.PageInfo;
import com.imjcker.manager.charge.po.BillingRules;
import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.model.PageInfo;
import com.imjcker.manager.charge.po.BillingRules;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BillingRulesMapper extends TkBaseMapper<BillingRules> {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uuid", column = "uuid"),
            @Result(property = "name", column = "name"),
            @Result(property = "billingType", column = "billing_type"),
            @Result(property = "billingCycle", column = "billing_cycle"),
            @Result(property = "billingCycleLimit", column = "billing_cycle_limit"),
            @Result(property = "billingMode", column = "billing_mode"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "status", column = "status")
    })
    @Select({"<script>",
            "select * from billing_rules",
            "WHERE",
            "<if test='name != null'>",
            "name like CONCAT('%','${name}','%')  AND",
            "</if>",
            "<if test='billingMode != null'>",
            "billing_mode =#{billingMode}  AND",
            "</if>",
            " status=1 ORDER BY update_time desc,create_time desc",
            " limit ${(currentPage-1)*pageSize},#{pageSize}",
            "</script>"})
    List<BillingRules> findByKeyword(@Param("name") String name, @Param("billingMode") Integer billingMode, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);

    @Select({"<script>",
            "select * from billing_rules ",
            "WHERE",
            "billing_type =#{billingType}  AND",
            "billing_mode =#{billingMode}  AND",
            " status=1",
            "</script>"})
    List<BillingRules> findRule(@Param("billingType") Integer billingType, @Param("billingMode") Integer billingMode);

    @Select({"<script>",
            "select * from billing_rules ",
            "WHERE",
            "billing_type =#{billingType}  AND",
            "billing_cycle =#{billingCycle}  AND",
            "billing_cycle_limit =#{billingCycleLimit}  AND",
            "billing_mode =#{billingMode}  AND",
            " status=1",
            "</script>"})
    List<BillingRules> find(@Param("billingType") Integer billingType, @Param("billingCycle") Integer billingCycle, @Param("billingCycleLimit") Long billingCycleLimit, @Param("billingMode") Integer billingMode);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uuid", column = "uuid"),
            @Result(property = "name", column = "name"),
            @Result(property = "billingType", column = "billing_type"),
            @Result(property = "billingCycle", column = "billing_cycle"),
            @Result(property = "billingCycleLimit", column = "billing_cycle_limit"),
            @Result(property = "billingMode", column = "billing_mode"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "status", column = "status")
    })
    @Select("select * from billing_rules where status=1 ORDER BY update_time desc,create_time desc limit ${(currentPage-1)*pageSize},#{pageSize} ")
    List<BillingRules> findByPage(PageInfo pageInfo);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uuid", column = "uuid"),
            @Result(property = "name", column = "name"),
            @Result(property = "billingType", column = "billing_type"),
            @Result(property = "billingCycle", column = "billing_cycle"),
            @Result(property = "billingCycleLimit", column = "billing_cycle_limit"),
            @Result(property = "billingMode", column = "billing_mode"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "status", column = "status")
    })
    @Select("select * from billing_rules where status=1 ORDER BY name desc ")
    List<BillingRules> query();

    @Select("select count(1) from billing_rules where status=1")
    Integer getTotalCount();

    @Select({"<script>",
            "select count(1) from billing_rules ",
            "WHERE",
            "<if test='name != null'>",
            "name like CONCAT('%','${name}','%')  AND",
            "</if>",
            "<if test='billingMode != null'>",
            "billing_mode = #{billingMode} AND",
            "</if>",
            "status =1 ",
            "</script>"})
    Integer getTotalCountByKeyword(@Param("name") String appKey, @Param("billingMode") Integer apiId);

    /*@Insert("insert into billing_rules (uuid,name,billing_type,billing_cycle,billing_cycle_limit,billing_mode,create_time,update_time,status) values (#{uuid},#{name},#{billingType},#{billingCycle},#{billingCycleLimit},#{billingMode},#{createTime},#{updateTime},#{status})")
    void insert(BillingRules billingRules);*/

    @Update("update billing_rules set status=0 where id=#{id}")
    void deleteById(Integer id);

    @Update("update billing_rules set name=#{name},billing_type=#{billingType},billing_cycle=#{billingCycle},billing_cycle_limit=#{billingCycleLimit},billing_mode=#{billingMode},update_time=#{updateTime} where id = #{id}")
    void update(BillingRules billingRules);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uuid", column = "uuid"),
            @Result(property = "name", column = "name"),
            @Result(property = "billingType", column = "billing_type"),
            @Result(property = "billingCycle", column = "billing_cycle"),
            @Result(property = "billingCycleLimit", column = "billing_cycle_limit"),
            @Result(property = "billingMode", column = "billing_mode"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "status", column = "status")
    })
    @Select("select * from billing_rules where uuid=#{uuid} and status=1")
    BillingRules selectByBillingRulesUuid(String uuid);

    @Select("select * from billing_rules where id=#{id} and status=1")
    BillingRules selectById(Integer id);
}
