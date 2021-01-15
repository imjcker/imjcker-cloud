package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiGroup;
import com.imjcker.manager.manage.po.ApiGroupExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiGroupMapper {
    int countByExample(ApiGroupExample example);

    int deleteByExample(ApiGroupExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiGroup record);

    int insertSelective(ApiGroup record);

    List<ApiGroup> selectByExample(ApiGroupExample example);

    ApiGroup selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiGroup record, @Param("example") ApiGroupExample example);

    int updateByExample(@Param("record") ApiGroup record, @Param("example") ApiGroupExample example);

    int updateByPrimaryKeySelective(ApiGroup record);

    int updateByPrimaryKey(ApiGroup record);
}
