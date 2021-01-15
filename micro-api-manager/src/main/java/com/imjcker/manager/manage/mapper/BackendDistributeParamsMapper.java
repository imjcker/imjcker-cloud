package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.BackendDistributeParams;
import com.imjcker.manager.manage.po.BackendDistributeParamsExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

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
