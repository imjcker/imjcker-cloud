package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiGroupRelation;
import com.imjcker.manager.manage.po.ApiGroupRelationExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiGroupRelationMapper {
    int countByExample(ApiGroupRelationExample example);

    int deleteByExample(ApiGroupRelationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiGroupRelation record);

    int insertSelective(ApiGroupRelation record);

    List<ApiGroupRelation> selectByExample(ApiGroupRelationExample example);

    ApiGroupRelation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiGroupRelation record, @Param("example") ApiGroupRelationExample example);

    int updateByExample(@Param("record") ApiGroupRelation record, @Param("example") ApiGroupRelationExample example);

    int updateByPrimaryKeySelective(ApiGroupRelation record);

    int updateByPrimaryKey(ApiGroupRelation record);

    ApiGroupRelation selectByApiId(Integer apiId);
}
