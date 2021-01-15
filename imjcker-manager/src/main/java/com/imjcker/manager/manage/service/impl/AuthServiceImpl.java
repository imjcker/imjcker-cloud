package com.imjcker.manager.manage.service.impl;

import com.imjcker.manager.manage.mapper.AuthMapper;
import com.imjcker.manager.manage.service.AuthService;
import com.imjcker.manager.manage.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImpl implements AuthService {

    private static Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 客户列表显示
     *
     * @param jsonObject
     * @return
     */

//    public Map<String, Object> show(JSONObject jsonObject) {
//        Map<String, Object> map = new HashMap<>();
//        Integer currPage = jsonObject.getInteger("currPage");
//        Integer pageSize = jsonObject.getInteger("pageSize");
//        PageInfo pageInfo = new PageInfo(currPage, pageSize);
//        List<CompanyAppsAuth> appList = authMapper.findByPage(pageInfo);
//        Integer totalCount = authMapper.getTotalCount();
//        pageInfo.setTotalCount(totalCount);
//        map.put("list", appList);
//        map.put("currPage", pageInfo.getCurrentPage());
//        map.put("pageSize", pageInfo.getPageSize());
//        map.put("totalCount", pageInfo.getTotalCount());
//        map.put("totalPage", pageInfo.getTotalPage());
//        return map;
//    }
//
//    /**
//     * 删除客户
//     *
//     * @param
//     * @return
//     */
//    @Override
//    public boolean delete(Integer id) {
//        List<CompanyAppsAuth> list = authMapper.findAppKeyAndApiId(null,null,id);
//        if (!list.isEmpty() && list.size()>0){
//            CompanyAppsAuth appsAuth = list.get(0);
//            //删除客户appKey存在的合约
//            authMapper.deleteById(id);
//            String appKey = appsAuth.getAppKey();
//            Integer apiId = appsAuth.getApiId();
//            //同步更新redis缓存
//            String authKey = "auth:" + appKey + ":" + apiId;
//            RedisUtil.delete(authKey);
//        }else {
//            throw new DataValidationException(ExceptionInfo.NOT_EXIST_ID);
//        }
//        return true;
//    }
//
//    /**
//     * 客户搜索
//     *
//     * @param jsonObject
//     * @return
//     */
//    @Override
//    public Map<String, Object> search(JSONObject jsonObject) {
//        Map<String, Object> map = new HashMap<>();
//        String appKey = jsonObject.getString("appKey");
//        Integer apiId = jsonObject.getInteger("apiId");
//        if (StringUtils.isBlank(appKey) && null ==apiId) {
//            return this.show(jsonObject);
//        }
//        Integer currPage = jsonObject.getInteger("currPage");
//        Integer pageSize = jsonObject.getInteger("pageSize");
//        PageInfo pageInfo = new PageInfo(currPage, pageSize);
//        List<CompanyAppsAuth> companyAppsAuthList = authMapper.findByKeyword(appKey, apiId,currPage, pageSize);//查数据库所有记录,模糊查询所有ip前缀的数据
//        int totalCount = authMapper.getTotalCountByKeyword(appKey,apiId);
//        pageInfo.setTotalCount(totalCount);
//        map.put("list", companyAppsAuthList);
//        map.put("currPage", pageInfo.getCurrentPage());
//        map.put("pageSize", pageInfo.getPageSize());
//        map.put("totalCount", pageInfo.getTotalCount());
//        map.put("totalPage", pageInfo.getTotalPage());
//        return map;
//    }
//
//    /**
//     * 新增客户
//     *
//     * @param jsonObject
//     * @return
//     * @throws ParseException
//     */
//    @Override
//    public boolean save(JSONObject jsonObject) {
//        checkParam(jsonObject);
//        //校验是否存在
//        CompanyAppsAuth appKey = jsonObject.toJavaObject(CompanyAppsAuth.class);
//        isExist(appKey.getAppKey(),appKey.getApiId());
//        appKey.setUpdateTime(System.currentTimeMillis());
//        appKey.setCreateTime(System.currentTimeMillis());
//        authMapper.insert(appKey);
//        //更新redis缓存
//        String appKeyStr =appKey.getAppKey();
//        Integer apiId = appKey.getApiId();
//        //同步更新redis缓存
//        String authKey = "auth:" + appKeyStr + ":" + apiId;
//        RedisUtil.setToCaches(authKey,appKey);
//        return true;
//    }
//
//    /**
//     * 编辑客户信息，不允许修改appKey
//     *
//     * @param jsonObject
//     * @return
//     * @throws
//     */
//    @Override
//    public boolean edit(JSONObject jsonObject) {
//        checkParam(jsonObject);
//        CompanyAppsAuth appKey = jsonObject.toJavaObject(CompanyAppsAuth.class);
//        appKey.setUpdateTime(System.currentTimeMillis());
//        authMapper.update(appKey);
//        //更新redis缓存
//        String appKeyStr =appKey.getAppKey();
//        Integer apiId = appKey.getApiId();
//        //同步更新redis缓存
//        String authKey = "auth:" + appKeyStr + ":" + apiId;
//        RedisUtil.setToCaches(authKey,appKey);
//        return true;
//    }
//
//    /**
//     * appKey+apiId是否存在有效关系
//     *
//     * @param
//     * @return
//     */
//    public void isExist(String appKeyStr,Integer apiId) {
//        if (StringUtils.isBlank(appKeyStr) || null == apiId){
//            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
//        }
//        List<CompanyAppsAuth> appKeyList = authMapper.findAppKeyAndApiId(appKeyStr,apiId,null);//精确查询数据库所有记录
//        if (null != appKeyList && appKeyList.size() > 0)
//            throw new DataValidationException(appKeyStr + ":"+apiId+ExceptionInfo.EXIST);
//    }
//
//    /**
//     * 参数检测
//     * @param jsonObject
//     */
//    public void checkParam(JSONObject jsonObject){
//        if (jsonObject.isEmpty())
//            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
//        CompanyAppsAuth appKey = jsonObject.toJavaObject(CompanyAppsAuth.class);
//        if (StringUtils.isBlank(appKey.getAppKey()) || null == appKey.getApiId())
//            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
//    }
//
    /**
     * 根据apiId删除记录,主要用于接口删除时同步删除该接口下的合约
     * @param apiId
     */
    @Override
    public void deleteByApiId(Integer apiId) {
        authMapper.deleteByApiId(apiId);
        redisService.keysDel( "auth:"+"*:"+apiId);
    }
}
