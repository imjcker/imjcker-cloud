package com.imjcker.manager.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.model.IndexVO;
import com.imjcker.manager.manage.vo.ShowVOSourceLogInfo;
import com.imjcker.manager.vo.CommonResult;
import com.imjcker.manager.vo.ResultStatusEnum;
import com.imjcker.manager.manage.service.CountService;
import com.imjcker.manager.elastic.model.SourceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 接口统计
 */
@RestController
@RequestMapping("/count")
public class CountController {

    @Autowired
    private CountService countService;

    @Value("${es.index}")
    private String index;

    @Value("${es.type}")
    private String type;

    @Value("${es.indexZuul}")
    private String indexZuul;

    @Value("${es.typeZuul}")
    private String typeZuul;

    /**
     * 按照接口展示调用量/成功量/失败量/成功调用的平均响应时间(默认时间为当天)
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/index")
    public CommonResult index(@RequestBody JSONObject jsonObject) {
        SourceQuery sourceQuery = jsonObject.toJavaObject(SourceQuery.class);
        IndexVO result = countService.index(index, type, sourceQuery);
        return CommonResult.success( result);
    }

    @PostMapping("/searchBySourceName")
    public CommonResult searchBySourceName(@RequestBody JSONObject jsonObject) {
        SourceQuery sourceQuery = jsonObject.toJavaObject(SourceQuery.class);
        IndexVO result = countService.searchBySourceName(index, type, sourceQuery);
        return CommonResult.success( result);
    }

    @PostMapping("/getInfoByUid")
    public CommonResult getInfoByUid(@RequestBody JSONObject jsonObject) {
        if (null != jsonObject) {
            String uid = jsonObject.getString("uid");
            JSONObject result = countService.getInfoByUid(index, type, indexZuul, typeZuul, uid);
            return CommonResult.success( result);
        }
        return CommonResult.error();
    }

    @PostMapping("/test")
    public CommonResult test(@RequestBody JSONObject jsonObject) {
        countService.test();
        return CommonResult.success();
    }

    /**
     * 失败量列表
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/errorListPage")
    public CommonResult errorListPage(@RequestBody JSONObject jsonObject) {
        SourceQuery sourceQuery = jsonObject.toJavaObject(SourceQuery.class);
        ShowVOSourceLogInfo result=countService.errorListPage(index, type, sourceQuery,1000);
        return CommonResult.success( result);
    }

    /**
     * 根据uid和服务查询日志平台日志
     * @param jsonObject
     * @return
     */
    @PostMapping("/queryLogByUid")
    public CommonResult queryLogByUid(@RequestBody JSONObject jsonObject) {
        List<String> result=countService.queryLogByUid(jsonObject);
        return CommonResult.success( result);
    }

}
