package com.imjcker.gateway.mapper;

import java.util.List;

import com.imjcker.gateway.po.RequestParamsVersions;
import org.apache.ibatis.annotations.Param;

import com.imjcker.gateway.po.RequestParamsVersionsExample;

public interface RequestParamsVersionsMapper {
    int countByExample(RequestParamsVersionsExample example);

    int deleteByExample(RequestParamsVersionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RequestParamsVersions record);

    int insertSelective(RequestParamsVersions record);

    List<RequestParamsVersions> selectByExample(RequestParamsVersionsExample example);

    RequestParamsVersions selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RequestParamsVersions record, @Param("example") RequestParamsVersionsExample example);

    int updateByExample(@Param("record") RequestParamsVersions record, @Param("example") RequestParamsVersionsExample example);

    int updateByPrimaryKeySelective(RequestParamsVersions record);

    int updateByPrimaryKey(RequestParamsVersions record);
}
