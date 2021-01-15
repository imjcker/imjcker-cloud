package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.Apiresultsettings;
import com.imjcker.manager.manage.po.ApiresultsettingsExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiresultsettingsMapper {
    int countByExample(ApiresultsettingsExample example);

    int deleteByExample(ApiresultsettingsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Apiresultsettings record);

    int insertSelective(Apiresultsettings record);

    List<Apiresultsettings> selectByExample(ApiresultsettingsExample example);

    Apiresultsettings selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Apiresultsettings record, @Param("example") ApiresultsettingsExample example);

    int updateByExample(@Param("record") Apiresultsettings record, @Param("example") ApiresultsettingsExample example);

    int updateByPrimaryKeySelective(Apiresultsettings record);

    int updateByPrimaryKey(Apiresultsettings record);
}
