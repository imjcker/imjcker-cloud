package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.AutoTestExample;
import com.imjcker.manager.manage.po.AutoTestResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AutoTestMapper {

    int insert(AutoTestResult record);

    List<AutoTestResult> selectByExample(AutoTestExample example);

}
