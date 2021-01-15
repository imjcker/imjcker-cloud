package com.imjcker.manager.manage.service;

import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.po.ApiOfflineRecord;
import com.imjcker.manager.manage.vo.ApiOfflineRecordVO;

public interface ApiOfflineRecordService {
      PageInfo<ApiOfflineRecord> findForPage(ApiOfflineRecordVO apiOfflineRecordVO);
}
