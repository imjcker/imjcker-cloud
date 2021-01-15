package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiOfflineRecord;
import com.imjcker.manager.manage.po.ApiOfflineRecordExample;
import com.imjcker.manager.manage.vo.ApiOfflineRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiOfflineRecordMapper {
    long countByExample(ApiOfflineRecordExample example);

    int deleteByExample(ApiOfflineRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiOfflineRecord record);

    int insertSelective(ApiOfflineRecord record);

    List<ApiOfflineRecord> selectByExample(ApiOfflineRecordExample example);

    ApiOfflineRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiOfflineRecord record, @Param("example") ApiOfflineRecordExample example);

    int updateByExample(@Param("record") ApiOfflineRecord record, @Param("example") ApiOfflineRecordExample example);

    int updateByPrimaryKeySelective(ApiOfflineRecord record);

    int updateByPrimaryKey(ApiOfflineRecord record);
    List<ApiOfflineRecordVO> findForPage(ApiOfflineRecordExample example);
}
