package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.RequestParamsVersionsExample;
import com.imjcker.manager.manage.po.RequestParamsVersionsLatest;

import java.util.List;

/**
 *  接口最新版本接口参数信息
 */
public interface RequestParamsVersionsLatestMapper {
    /**
     * 根据apiId查询接口信息
     * @param apiId
     * @return
     */
    Integer selectByPrimaryKey(Integer apiId);

    /**
     * 插入最新版本接口参数
     * @param record
     * @return
     */
    int insert(RequestParamsVersionsLatest record);

    /**
     * 更新最新版本接口参数
     * @param record
     * @return
     */
    int updateByPrimaryKey(RequestParamsVersionsLatest record);

    List<RequestParamsVersionsLatest> selectByExample(RequestParamsVersionsExample example);

    Integer deleteByPrimaryKey(Integer apiId);

    /**
     * 版本切换中根据apiId和requestParamsId更新RequestParamsVersionsLatest表数据
     * @param record
     * @return
     */
    int updateByApiIdAndRequestParamsId(RequestParamsVersionsLatest record);

    List<RequestParamsVersionsLatest> selectApiByApiId(Integer apiId);
}
