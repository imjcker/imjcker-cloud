package com.imjcker.api.handler.mapper;

import com.imjcker.api.handler.po.BackendRequestParamsVersions;
import com.imjcker.api.handler.po.BackendRequestParamsVersionsExample;
import com.imjcker.api.handler.po.BackendRequestParamsVersions;
import com.imjcker.api.handler.po.BackendRequestParamsVersionsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BackendRequestParamsVersionsMapper {
    int countByExample(BackendRequestParamsVersionsExample example);

    int deleteByExample(BackendRequestParamsVersionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BackendRequestParamsVersions record);

    int insertSelective(BackendRequestParamsVersions record);

    List<BackendRequestParamsVersions> selectByExample(BackendRequestParamsVersionsExample example);

    BackendRequestParamsVersions selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BackendRequestParamsVersions record, @Param("example") BackendRequestParamsVersionsExample example);

    int updateByExample(@Param("record") BackendRequestParamsVersions record, @Param("example") BackendRequestParamsVersionsExample example);

    int updateByPrimaryKeySelective(BackendRequestParamsVersions record);

    int updateByPrimaryKey(BackendRequestParamsVersions record);
}
