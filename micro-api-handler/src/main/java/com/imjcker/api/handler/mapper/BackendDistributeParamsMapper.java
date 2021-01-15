package com.imjcker.api.handler.mapper;

import com.imjcker.api.handler.po.BackendDistributeParams;
import com.imjcker.api.handler.po.BackendDistributeParamsExample;
import com.imjcker.api.handler.po.BackendDistributeParams;
import com.imjcker.api.handler.po.BackendDistributeParamsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BackendDistributeParamsMapper {
    int countByExample(BackendDistributeParamsExample example);

    int deleteByExample(BackendDistributeParamsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BackendDistributeParams record);

    int insertSelective(BackendDistributeParams record);

    List<BackendDistributeParams> selectByExample(BackendDistributeParamsExample example);

    BackendDistributeParams selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BackendDistributeParams record, @Param("example") BackendDistributeParamsExample example);

    int updateByExample(@Param("record") BackendDistributeParams record, @Param("example") BackendDistributeParamsExample example);

    int updateByPrimaryKeySelective(BackendDistributeParams record);

    int updateByPrimaryKey(BackendDistributeParams record);
}
