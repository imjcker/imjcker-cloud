package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiResultEtlVersions;
import com.imjcker.manager.manage.po.ApiResultEtlVersionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiResultEtlVersionsMapper {
    int countByExample(ApiResultEtlVersionsExample example);

    int deleteByExample(ApiResultEtlVersionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiResultEtlVersions record);

    int insertSelective(ApiResultEtlVersions record);

    List<ApiResultEtlVersions> selectByExample(ApiResultEtlVersionsExample example);

    ApiResultEtlVersions selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiResultEtlVersions record, @Param("example") ApiResultEtlVersionsExample example);

    int updateByExample(@Param("record") ApiResultEtlVersions record, @Param("example") ApiResultEtlVersionsExample example);

    int updateByPrimaryKeySelective(ApiResultEtlVersions record);

    int updateByPrimaryKey(ApiResultEtlVersions record);
}
