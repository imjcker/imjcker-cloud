package com.imjcker.manager.manage.controller;

/**
 * Created by lilinfeng on 2017/7/11.
 */

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.helper.ExceptionHelper;
import com.imjcker.manager.manage.po.Api;
import com.imjcker.manager.manage.po.query.ApiQuery;
import com.imjcker.manager.manage.po.query.AppQuery;
import com.imjcker.manager.manage.vo.QueryApiApp;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import com.imjcker.manager.manage.po.ApiAccreditApp;
import com.imjcker.manager.manage.po.ApiFindApp;
import com.imjcker.manager.manage.po.App;
import com.imjcker.manager.manage.service.impl.AppOldService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/appOld")
public class AppOldController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Resource
    private AppOldService appOldService;

    /**
     * index页面
     */
    @ResponseBody
    @RequestMapping(value = "/index")
    public CommonResult index(@RequestBody JSONObject jsonObject) {
        AppQuery query = jsonObject.toJavaObject(AppQuery.class);
        String appName=query.getAppName();
        if(StringUtils.isNotBlank(appName)) query.setAppName(appName.replace("_","\\_"));
        List<App> list = appOldService.queryByPage(query);
        query.setElements(list);
        return new CommonResult(ResultStatusEnum.SUCCESS, query);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    public CommonResult delete(@RequestBody JSONObject jsonObject) {
        App app = jsonObject.toJavaObject(App.class);
        try {
            appOldService.delete(app);
        } catch (Exception e) {
            LOG.error("AppController delete error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addApp(@RequestBody JSONObject jsonObject) {
        App newApp = jsonObject.toJavaObject(App.class);
        try {
            boolean flag = appOldService.checkName(newApp.getAppName());
            if (!flag) return new CommonResult(ResultStatusEnum.NAME_REPEAT, null);
            appOldService.addNew(newApp);
        } catch (Exception e) {
            LOG.error("AppController add error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateApp(@RequestBody JSONObject jsonObject) {
        App updateApp = jsonObject.toJavaObject(App.class);
        try {
            appOldService.update(updateApp);
        } catch (Exception e) {
            LOG.error("AppController updateLevel error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * 更新APP的策略（包含解除策略）
     */
    @RequestMapping(value = "/updateUuid", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateUuid(@RequestBody JSONObject jsonObject) {
        App updateApp = jsonObject.toJavaObject(App.class);
        try {
            appOldService.updateUuid(updateApp);
        } catch (Exception e) {
            LOG.error("AppController updateUuid error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * 不管前台传递过来啥（id,name）都是用这种形式 查询 用到的地方：api列表授权操作
     */
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult find(@RequestBody JSONObject jsonObject) {
        App app = jsonObject.toJavaObject(App.class);
        try {
            List<App> apps = appOldService.findApps(app);
            return new CommonResult(ResultStatusEnum.SUCCESS, apps);
        } catch (Exception e) {
            LOG.error("AppController findApp error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }

    /**
     * 授权_app页面_app授权api
     */
    @RequestMapping(value = "/appToApi", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult accredit(@RequestBody JSONObject jsonObject) {
        ApiAccreditApp record = jsonObject.toJavaObject(ApiAccreditApp.class);
        String apiIds_str = record.getApiIds();
        String[] apiIds = apiIds_str.split(",");
        Integer env = record.getEnv();
        Integer appId = record.getAppId();
        try {
            for (String apiId : apiIds) {
                ApiFindApp apiFindApp = new ApiFindApp();
                apiFindApp.setEnv(env);
                apiFindApp.setApiId(Integer.parseInt(apiId));
                apiFindApp.setAppCertificationId(appId);
                int total = appOldService.countApiFindApp(apiFindApp);
                if (total > 0) {
                    continue;
                }
                appOldService.accredit(apiFindApp);
            }
            return new CommonResult(ResultStatusEnum.SUCCESS, null);
        } catch (Exception e) {
            LOG.error("AppController appToApi error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }

    /**
     * 查找APP所对应的所有的授权API 应用场景：app列表 点击app详情 初始化已经授权的api
     */
    @RequestMapping(value = "/findApis", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult findApis(@RequestBody JSONObject jsonObject) {
        ApiQuery apiQuery = jsonObject.toJavaObject(ApiQuery.class);
        try {
            List<Api> list = appOldService.findApis(apiQuery);
            apiQuery.setElements(list);
            return new CommonResult(ResultStatusEnum.SUCCESS, apiQuery);
        } catch (Exception e) {
            LOG.error("AppController findApis error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }

    /**
     * 应用管理-已授权的api-解除授权
     */
    @RequestMapping(value = "/unAccredit", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult unAccredit(@RequestBody JSONObject jsonObject) {
        ApiFindApp apiFindApp = jsonObject.toJavaObject(ApiFindApp.class);
        try {
            appOldService.unApiAppRelation(apiFindApp);
        } catch (Exception e) {
            LOG.error("AppController unAccredit error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * APP详情
     */
    @RequestMapping(value = "/details", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult findOne(@RequestBody JSONObject jsonObject) {
        App app = jsonObject.toJavaObject(App.class);
        try {
            app = appOldService.findOne(app);
            return new CommonResult(ResultStatusEnum.SUCCESS, app);
        } catch (Exception e) {
            LOG.error("AppController findApp error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }

    /**
     * APP授权页面 查找所有可以授权的APi
     */
    @ResponseBody
    @RequestMapping(value = "/findNoAccreditApis", method = RequestMethod.POST)
    public CommonResult findAllApis(@RequestBody JSONObject jsonObject) {
        QueryApiApp queryApiApp = jsonObject.toJavaObject(QueryApiApp.class);
        String apiName=queryApiApp.getApiName();
        if(StringUtils.isNotBlank(apiName)) queryApiApp.setApiName(apiName.replace("_","\\_"));
        try {
            List<Api> list = appOldService.findNoAccreditApis(queryApiApp);
            return new CommonResult(ResultStatusEnum.SUCCESS, list);
        } catch (Exception e) {
            LOG.error("AppController findNoAccreditApis error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }
    /**检验appname是否重复*/
    @ResponseBody
    @RequestMapping(value = "/checkName", method = RequestMethod.POST)
    public CommonResult checkName(@RequestBody JSONObject jsonObject) {
        String name = jsonObject.getString("appName");
        boolean flag = true;
        if (StringUtils.isNotBlank(name)) {
            try {
                flag = appOldService.checkName(name);
            } catch (Exception e) {
                LOG.error("AppController checkName error...", e);
                return ExceptionHelper.handleException(e, null, 0);
            }
        }
        return new CommonResult(ResultStatusEnum.SUCCESS, flag);
    }
}
