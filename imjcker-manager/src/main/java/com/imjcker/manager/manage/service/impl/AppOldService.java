package com.imjcker.manager.manage.service.impl;

import com.google.common.base.Preconditions;
import com.imjcker.manager.manage.po.Api;
import com.imjcker.manager.manage.po.ApiFindApp;
import com.imjcker.manager.manage.po.App;
import com.imjcker.manager.manage.po.query.ApiQuery;
import com.imjcker.manager.manage.po.query.AppQuery;
import com.imjcker.manager.manage.vo.QueryApiApp;
import com.lemon.common.util.UUIDUtil;
import com.lemon.common.vo.CommonStatus;
import com.imjcker.manager.manage.mapper.ApiAppMapper;
import com.imjcker.manager.manage.mapper.ApiMapper;
import com.imjcker.manager.manage.mapper.AppOldMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lilinfeng on 2017/7/11.
 */
@Service
public class AppOldService {
    @Autowired
    private AppOldMapper appMapper;
    @Autowired
    private ApiAppMapper apiAppMapper;
    @Autowired
    private ApiMapper apiMapper;

    public List queryByPage(AppQuery query) {
        //设置总数并初始化page
        query.setCount(this.queryCount(query));
        List<App> apps=appMapper.queryByPage(query);
        return apps;
    }
    public List queryAppPageByApiId(AppQuery query) {
        query.setCount(appMapper.queryCountByApiId(query,query.getApiId()));
        List<App> apps=appMapper.queryPageByApiId(query,query.getApiId());
        return apps;
    }
    public int queryCount(AppQuery query) {
        return appMapper.queryCount(query);
    }

    public void delete(App app) {
        appMapper.delete(app);
        /**删除app 相应的也要删除 app-api 的中间表*/
        Long currentTime=System.currentTimeMillis();
        apiAppMapper.updateStatusByAppID(app.getId(),CommonStatus.DISENABLE,currentTime);
    }

    public void addNew(App app) {
        /**检查参数*/
        checkAddParam(app);
        /**appKey和appSecret自动生成*/
        app.setAppKey(buildAppKey());
        app.setAppSecret(UUIDUtil.creatUUID());
        app.setCreateTime(System.currentTimeMillis());
        app.setUpdateTime(System.currentTimeMillis());
        app.setStatus(CommonStatus.ENABLE);

        appMapper.insert(app);
    }
    private void checkAddParam(App entity) {
        Preconditions.checkState(
                 entity.getAppName() != null ,
                "addApp missing param");
    }
    private void checkAddParam(ApiFindApp entity) {
        Preconditions.checkState(
                entity.getApiId() != null && entity.getAppCertificationId() != null && entity.getEnv() != null,
                "addApiAppRelation missing param");
    }
    public void update(App entity) {
        /**检查参数*/
            checkAddParam(entity);
            entity.setUpdateTime(System.currentTimeMillis());
            appMapper.updateApp(entity);
    }
    public void updateUuid(App entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        /**更新策略UUID limitStrategyuuid*/
        appMapper.updateUuid(entity);
    }
    public App findOne(App app) {
        app=appMapper.select(app);
        if(app.getLimitStrategyuuid()!=null){
            app.setStrategy(appMapper.selectStrategyByUuid(app.getLimitStrategyuuid()));
        }
        return app;
    }
    public List<App> findApps(App app) {
       List<App> apps=appMapper.selectList(app);
        return apps;
    }
    public List<Api> findApis(ApiQuery query) {
        /**分页查询api*/
        query.setCount(this.queryCountByApp(query));
        List<Api> apiList=apiMapper.queryPageByApp(query);
        if(CollectionUtils.isEmpty(apiList)){
            return apiList;
        }
        /**设置分组名称*/
        for(Api api:apiList){
            api.setApiGroupName(apiMapper.findGroupName(api));
        }
            return apiList;
        /**java8 版本*/
/*      ApiFindApp apiAppRelation=new ApiFindApp();
        apiAppRelation.setAppCertificationId(appId);
        *//**通过APP Id在关系表中找到所有的关系（关系中包含所有的api id）*//*
        List<ApiFindApp> list=apiAppMapper.selectList(apiAppRelation);
        *//**获取到所有的apiId*//*
        List<Integer> apiIdList=list.stream().map(d->d.getApiId()).distinct().collect(toList());
        if(CollectionUtils.isEmpty(apiIdList)){
            return new ArrayList<Api>();
        }
        *//**下面的代码类似于分页查询*//*
        query.setCount(apiMapper.queryCountByIds(query,apiIdList));
        List<Api> apis=apiMapper.queryPageByIds(query,apiIdList);
        if(CollectionUtils.isEmpty(apis)){
            return apis;
        }
        *//**组装 分组名称 给 apis*//*
        apis.forEach(api->{
            api.setApiGroupName(apiMapper.findGroupName(api));
        });
        return  apis;*/
    }
    /**授权*/
    public void accredit(ApiFindApp record) {
        checkAddParam(record);
        record.setUpdateTime(System.currentTimeMillis());
        record.setCreateTime(System.currentTimeMillis());
        record.setStatus(CommonStatus.ENABLE);
        apiAppMapper.insert(record);
    }
    /**找到所有的app 如果是已经授权过的app  设置 isAccreditApi 为true*/
    public List<App> findNotAccredit( AppQuery query) {
        query.setCount(this.queryCount(query));
        List<App> appList= appMapper.queryByPage(query);


        /**java8版本   线上目前不支持*/
/*        appList.forEach(d->{
            int sum= apiAppMapper.checkAccredit(query.getEnv(),query.getApiId(),d.getId());
            d.setAccreditApi(sum==0?false:true);
        });*/
        for(App app:appList){
            int sum= apiAppMapper.checkAccredit(query.getEnv(),query.getApiId(),app.getId());
            app.setAccreditApi(sum==0?false:true);
        }
        return appList;
    }
    /**解除api-app之间的关系*/
    public void unApiAppRelation(ApiFindApp apiFindApp) {
        Long currentTime=System.currentTimeMillis();
        apiAppMapper.updateStatus(apiFindApp,CommonStatus.DISENABLE,currentTime);
    }
    private String buildAppKey(){
        int result=0;
        java.util.Random random=new java.util.Random();// 定义随机类
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<10;i++){
            result=random.nextInt(10);// 返回[0,10)集合中的整数，注意不包括10
            sb.append(result);
        }
        return sb.toString();
    }

    public int countApiFindApp(ApiFindApp record) {
        return apiAppMapper.countApiFindApp(record);
    }

    public Api findApiByApiId(Api api) {
       return apiMapper.select(api);
    }

    public List<Api> findNoAccreditApis(QueryApiApp queryApiApp) {
        List<Api> apiList= apiMapper.queryApis(queryApiApp.getApiName());
        ArrayList<Api> deleList=new ArrayList();
        for(Api api:apiList){
            int sum= apiAppMapper.checkAccredit(queryApiApp.getEnv(),api.getId(),queryApiApp.getAppId());
            if(sum>0) deleList.add(api);
        }
        /**移除已经授权的api*/
        for(Api api:deleList){
            apiList.remove(api);
        }
        /**设置分组名称*/
        for(Api api:apiList){
            api.setApiGroupName(apiMapper.findGroupName(api));
        }
        return apiList;
    }

    public List<App> findNoAccreditApps(QueryApiApp queryApiApp) {
        List<App> appList= appMapper.queryApps(queryApiApp.getAppName());
        ArrayList<App> deleList=new ArrayList();
        for(App app:appList){
            int sum= apiAppMapper.checkAccredit(queryApiApp.getEnv(),queryApiApp.getApiId(),app.getId());
            if(sum>0) deleList.add(app);
        }
        /**移除已经授权的api*/
        for(App app:deleList){
            appList.remove(app);
        }
        return appList;
    }

    public int queryCount(ApiQuery query) {
        return apiMapper.queryCount(query);
    }
    public int queryCountByApp(ApiQuery query) {
        return apiMapper.queryCountByApp(query);
    }

    public boolean checkName(String name) {
        int num=appMapper.countName(name);
        return num>0?false:true;
    }
}
