package com.imjcker.gateway.mapper;

import com.imjcker.gateway.po.RateLimitAppkey;
import com.imjcker.gateway.po.RateLimitAppkeyExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RateLimitAppkeyMapper {
    long countByExample(RateLimitAppkeyExample example);

    int deleteByExample(RateLimitAppkeyExample example);

    int deleteByPrimaryKey(String appKey);

    int insert(RateLimitAppkey record);

    int insertSelective(RateLimitAppkey record);

    List<RateLimitAppkey> selectByExample(RateLimitAppkeyExample example);

    RateLimitAppkey selectByPrimaryKey(String appKey);

    int updateByExampleSelective(@Param("record") RateLimitAppkey record, @Param("example") RateLimitAppkeyExample example);

    int updateByExample(@Param("record") RateLimitAppkey record, @Param("example") RateLimitAppkeyExample example);

    int updateByPrimaryKeySelective(RateLimitAppkey record);

    int updateByPrimaryKey(RateLimitAppkey record);
}
