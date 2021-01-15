package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.SystemErrorCode;
import com.imjcker.manager.manage.po.SystemErrorCodeExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SystemErrorCodeMapper {
    int countByExample(SystemErrorCodeExample example);

    int deleteByExample(SystemErrorCodeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SystemErrorCode record);

    int insertSelective(SystemErrorCode record);

    List<SystemErrorCode> selectByExample(SystemErrorCodeExample example);

    SystemErrorCode selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SystemErrorCode record, @Param("example") SystemErrorCodeExample example);

    int updateByExample(@Param("record") SystemErrorCode record, @Param("example") SystemErrorCodeExample example);

    int updateByPrimaryKeySelective(SystemErrorCode record);

    int updateByPrimaryKey(SystemErrorCode record);
}
