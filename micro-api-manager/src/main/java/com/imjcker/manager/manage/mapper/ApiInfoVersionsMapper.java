package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiInfoVersions;
import com.imjcker.manager.manage.po.ApiInfoVersionsExample;
import com.imjcker.manager.manage.po.ApiInfoVersionsWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiInfoVersionsMapper {
    int countByExample(ApiInfoVersionsExample example);

    int deleteByExample(ApiInfoVersionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiInfoVersionsWithBLOBs record);

    int insertSelective(ApiInfoVersionsWithBLOBs record);

    List<ApiInfoVersionsWithBLOBs> selectByExampleWithBLOBs(ApiInfoVersionsExample example);

    List<ApiInfoVersions> selectByExample(ApiInfoVersionsExample example);

    ApiInfoVersionsWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiInfoVersionsWithBLOBs record, @Param("example") ApiInfoVersionsExample example);

    int updateByExampleWithBLOBs(@Param("record") ApiInfoVersionsWithBLOBs record, @Param("example") ApiInfoVersionsExample example);

    int updateByExample(@Param("record") ApiInfoVersions record, @Param("example") ApiInfoVersionsExample example);

    int updateByPrimaryKeySelective(ApiInfoVersionsWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ApiInfoVersionsWithBLOBs record);

    int updateByPrimaryKey(ApiInfoVersions record);

}
