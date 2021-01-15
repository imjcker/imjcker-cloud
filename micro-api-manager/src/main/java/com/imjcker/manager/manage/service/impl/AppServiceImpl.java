package com.imjcker.manager.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.model.PageInfo;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.vo.CompanyApp;
import com.imjcker.manager.manage.mapper.AppMapper;
import com.imjcker.manager.manage.service.AppService;
import com.imjcker.manager.manage.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class AppServiceImpl implements AppService {

    private static Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 客户列表显示
     *
     * @param jsonObject
     * @return
     */

    public Map<String, Object> show(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        Integer currPage = jsonObject.getInteger("currPage");
        Integer pageSize = jsonObject.getInteger("pageSize");
        PageInfo pageInfo = new PageInfo(currPage, pageSize);
        List<CompanyApp> appList = appMapper.findByPage(pageInfo);
        Integer totalCount = appMapper.getTotalCount();
        pageInfo.setTotalCount(totalCount);
        map.put("list", appList);
        map.put("currPage", pageInfo.getCurrentPage());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("totalCount", pageInfo.getTotalCount());
        map.put("totalPage", pageInfo.getTotalPage());
        return map;
    }

    /**
     * 删除客户，注意需要同步删除客户绑定的接口调用权限
     * @param
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        try {
            //查找对应的客户appKey
            CompanyApp app = appMapper.selectById(id);
            String appKey = app.getAppKey();
            //删除客户appKey
            appMapper.deleteById(id);
            //删除客户绑定的合约
            appMapper.deleteByAppKey(appKey);
            //删除合约缓存
            redisService.keysDel( "auth:"+appKey+":*");
        } catch (Exception e) {
            logger.error("delete appKey error:{}", e);
            return false;
        }
        return true;
    }

    /**
     * 客户搜索，搜索条件为空时，分页展示所有客户
     * @param jsonObject
     * @return
     */
    @Override
    public Map<String, Object> search(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        String appKey = jsonObject.getString("appKey");
        if (StringUtils.isBlank(appKey)) {
            return this.show(jsonObject);
        }
        Integer currPage = jsonObject.getInteger("currPage");
        Integer pageSize = jsonObject.getInteger("pageSize");
        PageInfo pageInfo = new PageInfo(currPage, pageSize);
        List<CompanyApp> whiteIp = appMapper.findByAppKey(appKey, currPage, pageSize);//查数据库所有记录,模糊查询所有ip前缀的数据
        int totalCount = appMapper.getTotalCountByAppKey(appKey);
        pageInfo.setTotalCount(totalCount);
        map.put("list", whiteIp);
        map.put("currPage", pageInfo.getCurrentPage());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("totalCount", pageInfo.getTotalCount());
        map.put("totalPage", pageInfo.getTotalPage());
        return map;
    }
    /**
     * 新增客户
     * @param jsonObject
     * @return
     */
    @Override
    public boolean save(JSONObject jsonObject) {
        //校验是否存在
        CompanyApp appKey = jsonObject.toJavaObject(CompanyApp.class);
        if (isExist(appKey.getAppKey())) {
            throw new DataValidationException(appKey.getAppKey() + ExceptionInfo.EXIST);
        }
        appKey.setUpdateTime(System.currentTimeMillis());
        appKey.setCreateTime(System.currentTimeMillis());
        appMapper.insert(appKey);
        return true;
    }

    /**
     * 编辑客户信息，不允许修改appKey
     *
     * @param jsonObject
     * @return
     * @throws ParseException
     */
    @Override
    public boolean edit(JSONObject jsonObject) {
        if (jsonObject.isEmpty()) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        CompanyApp appKey = jsonObject.toJavaObject(CompanyApp.class);
        appKey.setUpdateTime(System.currentTimeMillis());
        appMapper.update(appKey);
        return true;
    }

    /**
     * appKey是否存在
     *
     * @param
     * @return
     */
    public boolean isExist(String appKeyStr) {
        if (StringUtils.isBlank(appKeyStr)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        List<CompanyApp> appKeyList = appMapper.findAppKey(appKeyStr);//查数据库所有记录,模糊查询所有ip前缀的数据
        if (null != appKeyList && appKeyList.size() > 0)
            return true;
        return false;
    }

    /**
     * 查找所有appKey，不分页
     * @return
     */
    @Override
    public List<CompanyApp> findApp() {
        List<CompanyApp> findAll = appMapper.findAll();
        return findAll;
    }


}
