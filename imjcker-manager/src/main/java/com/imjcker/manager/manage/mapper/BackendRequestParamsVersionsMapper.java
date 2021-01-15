package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.BackendRequestParamsVersions;
import com.imjcker.manager.manage.po.BackendRequestParamsVersionsExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
