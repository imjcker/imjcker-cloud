package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiCombination;
import com.imjcker.manager.manage.po.RequestParamsVersions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author WT
 * @Date 9:29 2019/8/23
 * @Version ModifyCombinationParamMapper v1.0
 * @Desicrption
 */
@Mapper
public interface ModifyCombinationParamMapper {
    List<ApiCombination> findCombInfByApiId(@Param("apiId") Integer apiId);

    void updateCombinationStatus(@Param("cId") Integer combinationId, @Param("apiId") Integer apiId,
                                  @Param("status") Integer status);

    List<ApiCombination> findCombInfByCid(@Param("cId") Integer combinationId);

    String findVersionIdByApiId(@Param("apiId") Integer apiId);

    void deleteCombInfVersionsParams(@Param("tableName") String tableName,@Param("versionId") String versionId);

    void deleteCombInfRequestVersionLatest(@Param("versionId") String versionId, @Param("apiId") Integer apiId);

    void updateCombInfParamsStatus(@Param("tableName") String tableName, @Param("apiId") Integer apiId, @Param("status") Integer status);

    List<RequestParamsVersions> findVersionsParamsByVersionId(@Param("versionId") String versionId);

    String findHttpPathWithApiId(@Param("apiId") Integer apiId);
}
