package com.imjcker.api.handler.mapper;


import com.imjcker.api.handler.po.RequestParamsVersions;
import com.imjcker.api.handler.po.RequestParamsVersionsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
