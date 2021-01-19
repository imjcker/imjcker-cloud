package com.imjcker.manager.manage.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

//import com.lemon.common.dao.proxy.MongoProxy;
import com.imjcker.manager.manage.helper.ExceptionHelper;
import com.imjcker.manager.manage.model.InterfaceCountModel;
import com.imjcker.manager.manage.po.AppCertification;
import com.imjcker.manager.manage.po.CurrentLimitStrategy;
import com.imjcker.manager.manage.po.query.AppQuery;
import com.imjcker.manager.manage.po.query.StrategyQuery;
import com.imjcker.manager.manage.vo.StrategyAuthQuery;
import com.lemon.common.exception.ExceptionInfo;
import com.imjcker.manager.manage.service.impl.InterfaceCountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lilinfeng on 2017/7/11.
 */

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.vo.BaseQuery;
import com.imjcker.manager.vo.CommonResult;
import com.imjcker.manager.vo.ResultStatusEnum;
import com.imjcker.manager.manage.po.App;
import com.imjcker.manager.manage.po.Strategy;
import com.imjcker.manager.manage.service.ApiService;
import com.imjcker.manager.manage.service.StrategyAuthService;
import com.imjcker.manager.manage.service.impl.StrategyService;

@RestController
@RequestMapping("/strategy")
public class StrategyController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Resource
    private StrategyService service;

    @Autowired
    private StrategyAuthService authService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private InterfaceCountService interfaceCountService;

    /**getApiCountVOList*/
    @RequestMapping(value = "/index",method = RequestMethod.POST)
    public CommonResult index(@RequestBody JSONObject jsonObject) {
        StrategyQuery query = jsonObject.toJavaObject(StrategyQuery.class);
        String limitName=query.getName();
        if(StringUtils.isNotBlank(limitName)) query.setName(limitName.replace("_","\\_"));
        List<Strategy> list=service.queryByPage(query);
        query.setElements(list);
        return CommonResult.success(query);
    }
    /**删除*/
    @RequestMapping("/delete")
    public CommonResult delete(@RequestBody JSONObject jsonObject) {
        Strategy strategy=jsonObject.toJavaObject(Strategy.class);
        Integer id=strategy.getId();
        //接口2.0---检查该策略是否已经与其他Api进行绑定
        CurrentLimitStrategy limitStrategy = service.findStrategyById(id);
        if (!apiService.checkStrategyUsed(limitStrategy.getUuid())) {
        	throw new BusinessException(ExceptionInfo.THIS_STRATEGY_ALREADY_BINDING);
        }
        try {
                service.delete(id);
        } catch (Exception e) {
            LOG.error("StrategyController delete error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
        return CommonResult.success();
    }
    /**新增*/
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public CommonResult addStrategy(@RequestBody JSONObject jsonObject) {
        Strategy newStrategy=jsonObject.toJavaObject(Strategy.class);
        //通过正则表达式验证名称是否符合规范
        if(!checkName(newStrategy.getName())) return new CommonResult(ResultStatusEnum.NAME_INVALID,null);
        //名称4-15字
        if (!(newStrategy.getName().length() >= 4 && newStrategy.getName().length() <= 15)) {
        	throw new DataValidationException(ExceptionInfo.LENGTH_ERROR_STRATEGY_NAME);
        }
        //判断名称是否重复
        boolean flag = service.checkName(newStrategy.getName());
        if (!flag) return new CommonResult(ResultStatusEnum.NAME_REPEAT, null);
        //验证流量次数不能大于1000W
        if(newStrategy.getNo()!=null && !checkNo(newStrategy.getNo())) return new CommonResult(ResultStatusEnum.NO_ERROR,null);
        try {
            service.addNew(newStrategy);
        } catch (Exception e) {
            LOG.error("StrategyController add error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
        return CommonResult.success();
    }
    /**更新*/
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public CommonResult updateStrategy(@RequestBody JSONObject jsonObject) {
        Strategy updateStrategy=jsonObject.toJavaObject(Strategy.class);
        if(!checkName(updateStrategy.getName())) return new CommonResult(ResultStatusEnum.NAME_INVALID,null);
        if(updateStrategy.getNo()!=null &&!checkNo(updateStrategy.getNo())) return new CommonResult(ResultStatusEnum.NO_ERROR,null);
        try {
            service.update(updateStrategy);
        } catch (Exception e) {
            LOG.error("StrategyController updateLevel error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
        return CommonResult.success();
    }
    /**流量控制-绑定的app列表-解除授权*/
    @RequestMapping(value = "/unAccredit", method = RequestMethod.POST)
    public CommonResult unAccredit(@RequestBody JSONObject jsonObject) {
        App app=jsonObject.toJavaObject(App.class);
        Integer appId=app.getId();
        try {
            service.unApiAppRelation(appId);
        } catch (Exception e) {
            LOG.error("StrategyController unAccredit error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
        return CommonResult.success();
    }
    /**查找策略所对应的所有绑定的APP*/
    @RequestMapping(value = "/findApps", method = RequestMethod.POST)
    public CommonResult findApps(@RequestBody JSONObject jsonObject) {
        AppQuery query=jsonObject.toJavaObject(AppQuery.class);
        try {
            List<App> list=service.findApp(query);
            query.setElements(list);
            return CommonResult.success(query);
        } catch (Exception e) {
            LOG.error("AppController findApis error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }
    /**查找所有的APP（包括已经绑定的，页面显示是灰色） 用于绑定APP操作*/
    @RequestMapping(value = "/findAllApps", method = RequestMethod.POST)
    public CommonResult findAllApps(@RequestBody JSONObject jsonObject) {
        AppQuery query=jsonObject.toJavaObject(AppQuery.class);
        try {
            List<App> list=service.findNotAccredit(query);
            query.setElements(list);
            return CommonResult.success(query);
        } catch (Exception e) {
            LOG.error("AppController findApis error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }

    /**授权*/
    @RequestMapping(value = "/accredit", method = RequestMethod.POST)
    public CommonResult accredit(@RequestBody JSONObject jsonObject) {
        App record=jsonObject.toJavaObject(App.class);
        try {
            String ids=record.getIds();
            String[] idArray=ids.split(",");
            for(String id:idArray){
                record.setId(Integer.parseInt(id));
                service.accredit(record);
            }
            return CommonResult.success();
        } catch (Exception e) {
            LOG.error("AppController accredit error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }
    /**策略详情*/
    @RequestMapping(value = "/details", method = RequestMethod.POST)
    public CommonResult find(@RequestBody JSONObject jsonObject) {
        Strategy strategy=jsonObject.toJavaObject(Strategy.class);
        try {
            strategy=service.findOne(strategy);
            return CommonResult.success(strategy);
        } catch (Exception e) {
            LOG.error("AppController findApp error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }
    //app分页列表
    @PostMapping("/getStrategyList")
    public CommonResult getStrategyList(@RequestBody JSONObject jsonObject){
    	BaseQuery query = jsonObject.toJavaObject(BaseQuery.class);
    	PageInfo<AppCertification> pageInfo = authService.getStrategyList(query);
    	return CommonResult.success( pageInfo);
    }
    //绑定app
    @PostMapping("/strategyAuth")
    public CommonResult strategyAuth(@RequestBody JSONObject jsonObject){
    	StrategyAuthQuery query = jsonObject.toJavaObject(StrategyAuthQuery.class);
    	Boolean res = authService.strategyAuth(query);
    	return CommonResult.success( res);
    }
    //得到所有已绑定的app
    @PostMapping("/getAllAuthed")
    public CommonResult getAllAuthed(@RequestBody JSONObject jsonObject){
    	String strategyUuid = jsonObject.getString("strategyUuid");
    	List<AppCertification> res = authService.getAllAuthed(strategyUuid);
    	return CommonResult.success( res);
    }
    //获取所有可用的策略
    @PostMapping("/getAllStrategy")
    public CommonResult getAllStrategy(@RequestBody JSONObject jsonObject){
    	String strategyName = jsonObject.getString("strategyName");
    	List<CurrentLimitStrategy> res = authService.getAllStrategy(strategyName);
    	return CommonResult.success( res);
    }
    //移除所有可用的策略
    @PostMapping("/removeApp")
    public CommonResult removeApp(@RequestBody JSONObject jsonObject){
    	Integer appId = jsonObject.getInteger("id");
    	boolean res = authService.removeApp(appId);
    	return CommonResult.success( res);
    }
    /**检验limitname是否重复*/
    @PostMapping(value = "/checkName")
    public CommonResult checkName(@RequestBody JSONObject jsonObject) {
        String name = jsonObject.getString("strategyName");
        boolean flag = true;
        if (StringUtils.isNotBlank(name)) {
            try {
                flag = service.checkName(name);
            } catch (Exception e) {
                LOG.error("StrategyController checkName error...", e);
                return ExceptionHelper.handleException(e, null, 0);
            }
        }
        return CommonResult.success( flag);
    }
    private Boolean checkName(String name){
        String regEx = "^[a-zA-Z\u4e00-\u9fa5][a-zA-Z0-9\u4e00-\u9fa5_]{3,49}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(name);
        return m.find();
    }
    /**no不能超过一千万*/
    private Boolean checkNo(Integer no){
        if(no<0) return false;
        return no>10000000?false:true;
    }

	/**
	 * @Title: 验证策略唯一性
	 * @return true-存在，false-不存在
	 */
    @PostMapping("/checkStrategyUnique")
    public CommonResult checkStrategyUnique(@RequestBody JSONObject jsonObject){
    	String name = jsonObject.getString("strategyName");
    	boolean res = authService.checkStrategyUnique(name);
    	return CommonResult.success( res);
    }

    @PostMapping("/interfaceCount")
    public CommonResult interfaceCount(@RequestBody JSONObject jsonObject){
        Map<String,Object> result = new HashMap<>();
        Long startTime = jsonObject.getTimestamp("startTime").getTime();
        Long endTime = jsonObject.getTimestamp("endTime").getTime();
        Integer timeUnit = jsonObject.getInteger("timeUnit");
        Long time = (endTime - startTime)/1000;
        List<InterfaceCountModel> list = interfaceCountService.findByConditions(startTime,endTime, jsonObject.getString("apiId"), jsonObject.getString("appKey"), jsonObject.getString("cached"), jsonObject.getString("resultStatus"));
        DecimalFormat df = new DecimalFormat("0.00");
        List<JSONObject> dataList;
        try {
            dataList = interfaceCountService.handleResult(list, time, timeUnit);
            result.put("freq",df.format(list.size()/InterfaceCountService.timeTransfer(time,timeUnit)));
        } catch (Exception e) {
            return new CommonResult(ResultStatusEnum.TIME_ERROR,null);
        }
        result.put("dataList",dataList);
        result.put("count",list.size());
        return CommonResult.success(result);
    }
}
