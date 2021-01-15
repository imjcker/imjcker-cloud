package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ServiceRouter;
import com.imjcker.manager.manage.po.ServiceRouterExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceRouterMapper {
    long countByExample(ServiceRouterExample example);

    int deleteByExample(ServiceRouterExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServiceRouter record);

    int insertSelective(ServiceRouter record);

    List<ServiceRouter> selectByExample(ServiceRouterExample example);

    ServiceRouter selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceRouter record, @Param("example") ServiceRouterExample example);

    int updateByExample(@Param("record") ServiceRouter record, @Param("example") ServiceRouterExample example);

    int updateByPrimaryKeySelective(ServiceRouter record);

    int updateByPrimaryKey(ServiceRouter record);
}
