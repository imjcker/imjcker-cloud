package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiInfoLatestExample;
import com.imjcker.manager.manage.po.ApiInfoVersions;
import com.imjcker.manager.manage.po.ApiInfoVersionsExample;
import com.imjcker.manager.manage.po.ApiInfoVersionsWithBLOBs;
import com.imjcker.manager.manage.vo.ApiInfoVersionLatestResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *   API最新版本信息Mapper
 */
public interface ApiInfoVersionsLatestMapper {

    /**
     *  根据apiId查询接口是否存在
     * @param id
     * @return
     */
    Integer  selectByApiId(Integer id);
    /**
     *  查询所有接口
     * @return
     */
    List<ApiInfoVersionsWithBLOBs>  selectAllApi();
    /**
     * 插入接口信息
     * @param record
     * @return
     */
    int insert(ApiInfoVersionsWithBLOBs record);

    /**
     * 更新接口（根据apiId）
     * @param record
     * @return
     */
    int updateByPrimaryKeyWithBLOBs(ApiInfoVersionsWithBLOBs record);

    /**
     * 根据条件查询接口信息
     * @param example
     * @return
     */
    List<ApiInfoVersions> selectByExample(ApiInfoVersionsExample example);

    /**
     * 更新接口
     * @param record
     * @param example
     * @return
     */
    int updateByExampleSelective(@Param("record") ApiInfoVersionsWithBLOBs record, @Param("example") ApiInfoVersionsExample example);

    /**
     * 根据apiId删除接口
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    List<ApiInfoVersionLatestResponse> selectApiInfoLatestForAutoTest(@Param("groupId") Integer groupId);

    /**
     * 查询列表
     * @param example
     * @return
     */
    List<ApiInfoVersionsWithBLOBs> selectByExampleWithBLOBs(ApiInfoVersionsExample example);

    int changeAutoTestStatusAsYes(@Param("apiId") Integer apiId);

    int changeAutoTestStatusAsNo(@Param("apiId") Integer apiId);

    List<ApiInfoVersionLatestResponse> selectApiInfoLatestByExample(ApiInfoLatestExample example);
}
