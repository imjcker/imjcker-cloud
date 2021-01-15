package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.RequestParams;
import com.imjcker.manager.manage.po.RequestParamsExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RequestParamsMapper {
    int countByExample(RequestParamsExample example);

    int deleteByExample(RequestParamsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RequestParams record);

    int insertSelective(RequestParams record);

    List<RequestParams> selectByExample(RequestParamsExample example);

    RequestParams selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RequestParams record, @Param("example") RequestParamsExample example);

    int updateByExample(@Param("record") RequestParams record, @Param("example") RequestParamsExample example);

    int updateByPrimaryKeySelective(RequestParams record);

    int updateByPrimaryKey(RequestParams record);

    /**
     * 切换版本中根据requestParamsId设置status=1
     * @param id
     * @return
     */
    int updateStatus2EnableById(Integer id);
    /**
     * 切换版本中根据requestParamsId设置status=2
     * @param id
     * @return
     */
    int updateStatus2DisableById(Integer id);
}
