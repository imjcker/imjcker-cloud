package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.AppCertification;
import com.imjcker.manager.manage.po.AppCertificationExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppCertificationMapper {
    int countByExample(AppCertificationExample example);

    int deleteByExample(AppCertificationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AppCertification record);

    int insertSelective(AppCertification record);

    List<AppCertification> selectByExampleWithBLOBs(AppCertificationExample example);

    List<AppCertification> selectByExample(AppCertificationExample example);

    AppCertification selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AppCertification record, @Param("example") AppCertificationExample example);

    int updateByExampleWithBLOBs(@Param("record") AppCertification record, @Param("example") AppCertificationExample example);

    int updateByExample(@Param("record") AppCertification record, @Param("example") AppCertificationExample example);

    int updateByPrimaryKeySelective(AppCertification record);

    int updateByPrimaryKeyWithBLOBs(AppCertification record);

    int updateByPrimaryKey(AppCertification record);
}
