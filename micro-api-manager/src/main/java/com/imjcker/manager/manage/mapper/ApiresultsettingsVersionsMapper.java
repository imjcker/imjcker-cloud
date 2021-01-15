package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiresultsettingsVersions;
import com.imjcker.manager.manage.po.ApiresultsettingsVersionsExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiresultsettingsVersionsMapper {
    int countByExample(ApiresultsettingsVersionsExample example);

    int deleteByExample(ApiresultsettingsVersionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiresultsettingsVersions record);

    int insertSelective(ApiresultsettingsVersions record);

    List<ApiresultsettingsVersions> selectByExample(ApiresultsettingsVersionsExample example);

    ApiresultsettingsVersions selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiresultsettingsVersions record, @Param("example") ApiresultsettingsVersionsExample example);

    int updateByExample(@Param("record") ApiresultsettingsVersions record, @Param("example") ApiresultsettingsVersionsExample example);

    int updateByPrimaryKeySelective(ApiresultsettingsVersions record);

    int updateByPrimaryKey(ApiresultsettingsVersions record);
}
