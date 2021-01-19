package com.imjcker.manager.manage.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.vo.ApiOfflineRecordVO;
import com.imjcker.manager.vo.CommonResult;
import com.imjcker.manager.manage.po.ApiOfflineRecord;
import com.imjcker.manager.manage.service.ApiOfflineRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/ApiOffline")
@RestController
public class ApiOfflineRecordController {
    @Autowired
    private ApiOfflineRecordService apiOfflineRecordService;
    @PostMapping("/findForPage")
    public CommonResult findOfflineRecord(@RequestBody JSONObject jsonObject){
        ApiOfflineRecordVO apiOfflineRecordvo=jsonObject.toJavaObject(ApiOfflineRecordVO.class) ;
        PageInfo<ApiOfflineRecord> forPage = apiOfflineRecordService.findForPage(apiOfflineRecordvo);
        return  CommonResult.success(forPage);
    }
}
