package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.model.BackendParamsResponse;
import com.imjcker.manager.manage.po.BackendRequestParams;
import com.imjcker.manager.manage.po.BackendRequestParamsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BackendRequestParamsMapper {
    int countByExample(BackendRequestParamsExample example);

    int deleteByExample(BackendRequestParamsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BackendRequestParams record);

    int insertSelective(BackendRequestParams record);

    List<BackendRequestParams> selectByExample(BackendRequestParamsExample example);

    BackendRequestParams selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BackendRequestParams record, @Param("example") BackendRequestParamsExample example);

    int updateByExample(@Param("record") BackendRequestParams record, @Param("example") BackendRequestParamsExample example);

    int updateByPrimaryKeySelective(BackendRequestParams record);

    int updateByPrimaryKey(BackendRequestParams record);

    List<BackendParamsResponse> selectCustomBackendParam(Map<String, Object> map);

    int updateStatus2EnableById(Integer id);

    int updateStatus2DisableById(Integer id);

    BackendRequestParams selectByRequestParamsId(Integer requestParamsId);
}
