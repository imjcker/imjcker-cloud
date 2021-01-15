package com.imjcker.gateway.mapper;

import com.imjcker.gateway.po.ApiInfoVersions;
import com.imjcker.gateway.po.ApiInfoVersionsExample;
import com.imjcker.gateway.po.BranchBankSourceAccount;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ApiInfoVersionsMapper {
    long countByExample(ApiInfoVersionsExample example);

    int deleteByExample(ApiInfoVersionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiInfoVersions record);

    int insertSelective(ApiInfoVersions record);

    List<ApiInfoVersions> selectByExampleWithBLOBs(ApiInfoVersionsExample example);

    List<ApiInfoVersions> selectByExample(ApiInfoVersionsExample example);

    ApiInfoVersions selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiInfoVersions record, @Param("example") ApiInfoVersionsExample example);

    int updateByExampleWithBLOBs(@Param("record") ApiInfoVersions record, @Param("example") ApiInfoVersionsExample example);

    int updateByExample(@Param("record") ApiInfoVersions record, @Param("example") ApiInfoVersionsExample example);

    int updateByPrimaryKeySelective(ApiInfoVersions record);

    int updateByPrimaryKeyWithBLOBs(ApiInfoVersions record);

    int updateByPrimaryKey(ApiInfoVersions record);

    String getHttpPathByApiId(@Param("apiId") Integer apiId);

    List<BranchBankSourceAccount> getBranchBankAccountList(@Param("apiGroupId") Integer apiGroupId,
                                                           @Param("appKey") String appKey);
}
