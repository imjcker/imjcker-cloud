package com.imjcker.gateway.mapper;

import com.imjcker.gateway.po.BillingRules;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BillingRulesMapper {

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
}
