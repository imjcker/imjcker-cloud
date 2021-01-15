package com.imjcker.manager.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.po.ApiOfflineRecord;
import com.imjcker.manager.manage.po.ApiOfflineRecordExample;
import com.imjcker.manager.manage.vo.ApiOfflineRecordVO;
import com.imjcker.manager.manage.mapper.ApiOfflineRecordMapper;
import com.imjcker.manager.manage.service.ApiOfflineRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiOfflineRecordServiceImpl implements ApiOfflineRecordService {
    @Autowired
    private ApiOfflineRecordMapper apiOfflineRecordMapper;
    @Override
    public PageInfo<ApiOfflineRecord> findForPage(ApiOfflineRecordVO apiOfflineRecordVO) {
          ApiOfflineRecordExample apiOfflineRecordExample=new ApiOfflineRecordExample();
        ApiOfflineRecordExample.Criteria criteria = apiOfflineRecordExample.createCriteria();
        if(apiOfflineRecordVO.getApiId()!=null){
            criteria.andApiIdEqualTo(apiOfflineRecordVO.getApiId());
        }
        if(apiOfflineRecordVO.getApiGroupId()!=null){
            criteria.andApiGroupIdEqualTo(apiOfflineRecordVO.getApiGroupId());
        }
        if(apiOfflineRecordVO.getStartTime()!=null){
            criteria.andCreateTimeBetween(apiOfflineRecordVO.getStartTime(),apiOfflineRecordVO.getEndTime());
        }


        return PageHelper.startPage(apiOfflineRecordVO).doSelectPageInfo(() -> apiOfflineRecordMapper.findForPage(apiOfflineRecordExample));
    }
}
