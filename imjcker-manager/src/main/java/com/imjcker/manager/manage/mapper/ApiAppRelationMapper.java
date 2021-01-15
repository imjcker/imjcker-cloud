package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiAppRelation;
import com.imjcker.manager.manage.po.ApiAppRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiAppRelationMapper {
    int countByExample(ApiAppRelationExample example);

    int deleteByExample(ApiAppRelationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiAppRelation record);

    int insertSelective(ApiAppRelation record);

    List<ApiAppRelation> selectByExample(ApiAppRelationExample example);

    ApiAppRelation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiAppRelation record, @Param("example") ApiAppRelationExample example);

    int updateByExample(@Param("record") ApiAppRelation record, @Param("example") ApiAppRelationExample example);

    int updateByPrimaryKeySelective(ApiAppRelation record);

    int updateByPrimaryKey(ApiAppRelation record);
}
