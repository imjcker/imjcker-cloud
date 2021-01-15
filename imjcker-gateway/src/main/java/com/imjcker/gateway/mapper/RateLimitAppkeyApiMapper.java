package com.imjcker.gateway.mapper;

import com.imjcker.gateway.po.RateLimitAppkeyApi;
import com.imjcker.gateway.po.RateLimitAppkeyApiExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RateLimitAppkeyApiMapper {
    long countByExample(RateLimitAppkeyApiExample example);

    int deleteByExample(RateLimitAppkeyApiExample example);

    int deleteByPrimaryKey(@Param("appKey") String appKey, @Param("apiId") Integer apiId);

    int insert(RateLimitAppkeyApi record);

    int insertSelective(RateLimitAppkeyApi record);

    List<RateLimitAppkeyApi> selectByExample(RateLimitAppkeyApiExample example);

    RateLimitAppkeyApi selectByPrimaryKey(@Param("appKey") String appKey, @Param("apiId") Integer apiId);

    int updateByExampleSelective(@Param("record") RateLimitAppkeyApi record, @Param("example") RateLimitAppkeyApiExample example);

    int updateByExample(@Param("record") RateLimitAppkeyApi record, @Param("example") RateLimitAppkeyApiExample example);

    int updateByPrimaryKeySelective(RateLimitAppkeyApi record);

    int updateByPrimaryKey(RateLimitAppkeyApi record);
}
