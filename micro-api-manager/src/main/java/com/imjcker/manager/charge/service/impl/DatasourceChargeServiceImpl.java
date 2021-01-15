package com.imjcker.manager.charge.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.charge.mapper.CompanyAppsAuthMapper;
import com.imjcker.manager.charge.mapper.DatasourceChargeMapper;
import com.imjcker.manager.charge.po.CompanyAppsAuth;
import com.imjcker.manager.charge.po.DatasourceCharge;
import com.imjcker.manager.charge.service.AuthService;
import com.imjcker.manager.charge.service.DatasourceChargeService;
import com.imjcker.manager.charge.vo.DatasourceChargeVO;
import com.imjcker.manager.charge.vo.request.ReqCheckUpdateDatasource;
import com.lemon.common.dao.util.RedisUtils;
import com.lemon.common.util.RedisKeyUtil;
import com.lemon.common.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ztzh_tanhh 2020/3/17
 **/
@Service
public class DatasourceChargeServiceImpl implements DatasourceChargeService {
    private final DatasourceChargeMapper datasourceChargeMapper;

    @Autowired
    public DatasourceChargeServiceImpl(DatasourceChargeMapper datasourceChargeMapper) {
        this.datasourceChargeMapper = datasourceChargeMapper;
    }

    @Autowired
    private CompanyAppsAuthMapper companyAppsAuthMapper;
    @Autowired
    private AuthService authService;

    @Override
    public PageInfo<DatasourceCharge> selectForPage(JSONObject jsonObject) {
        DatasourceChargeVO datasourceChargeVO = jsonObject.toJavaObject(DatasourceChargeVO.class);
        return PageHelper.startPage(datasourceChargeVO).doSelectPageInfo(() -> datasourceChargeMapper.selectForPage(datasourceChargeVO));
    }

    @Override
    public boolean save(JSONObject jsonObject) {
        DatasourceCharge datasourceCharge = jsonObject.toJavaObject(DatasourceCharge.class);
        if(datasourceCharge.getEndTime()==null || datasourceCharge.getStartTime()==null){
            throw new DataValidationException("时间不能为空");
        }
        if(datasourceCharge.getEndTime()<=datasourceCharge.getStartTime()){
            throw new DataValidationException("时间区间不正确");
        }
        this.checkUnique(datasourceCharge);
        datasourceCharge.setStatus(1);
        datasourceCharge.setStartTime(datasourceCharge.getStartTime());
        datasourceCharge.setEndTime(datasourceCharge.getEndTime()-1);
        int i = datasourceChargeMapper.insertSelective(datasourceCharge);
        if (i == 1) {
            RedisUtil.set("datasource_charge:"+datasourceCharge.getApiId(), datasourceCharge);
        }
        return i==1;
    }

    @Override
    public boolean update(Integer id) {
        JSONObject jsonObject = RedisUtil.get(RedisKeyUtil.getDataUpdateQuery(id));
        if(jsonObject==null){
            throw new BusinessException("该ID为通过核查或缓存已过期");
        }
        DatasourceCharge datasourceCharge = jsonObject.toJavaObject(DatasourceCharge.class);
        DatasourceCharge fromDB = datasourceChargeMapper.selectByPrimaryKey(datasourceCharge.getId());
        //if endTime changed, new endTime has to minus 1
        if (!fromDB.getEndTime().equals(datasourceCharge.getEndTime())) {
            datasourceCharge.setEndTime(datasourceCharge.getEndTime() - 1);
        }

        int i = datasourceChargeMapper.updateByPrimaryKeySelective(datasourceCharge);
        if (i == 1) {
            RedisUtil.set("datasource_charge:"+datasourceCharge.getApiId(), datasourceCharge);
            RedisUtil.delete(RedisKeyUtil.getDataUpdateQuery(datasourceCharge.getId()));
        }
        return i==1;
    }

    @Override
    public Map<String, BigDecimal> checkUpdateInfo(ReqCheckUpdateDatasource req) {
        DatasourceCharge fromDB = datasourceChargeMapper.selectByPrimaryKey(req.getId());
        if(fromDB.getPrice().compareTo(req.getPrice())>=0){
            RedisUtil.set(RedisKeyUtil.getDataUpdateQuery(req.getId()),req,5 * 60);
            //价格没有改大就跳过成本核算
            return new HashMap<>();
        }
        CompanyAppsAuth queryAuth = new CompanyAppsAuth();
        queryAuth.setApiId(fromDB.getApiId());
        queryAuth.setStatus(1);
        queryAuth.setEndTime(new Date().getTime());
        //有效绑定list
        List<CompanyAppsAuth> list = companyAppsAuthMapper.listBySome(queryAuth);
        Map<String,BigDecimal> map = new HashMap<>();
        DatasourceCharge update = new DatasourceCharge();
        BeanUtils.copyProperties(req,update);
        for (CompanyAppsAuth appsAuth : list) {
            try {
                authService.checkPrice(appsAuth,update);
            } catch (DayDeficitException e) {
                String message = e.getMessage();
                //String key = "每天调用即将亏损最大值:";
                delMap(map,message);
            } catch (PerDeficitException e) {
                String message = e.getMessage();
                //String key = "每笔调用即将亏损最大值:";
                delMap(map,message);
            } catch (ThanMaxException e) {
                String message = e.getMessage();
                //String key = "超过最大调用量条数:";
                delMap(map,message);
            }
        }
        //缓存请求参数。如果通过校验。 否则 上面流程会抛出异常
        RedisUtil.set(RedisKeyUtil.getDataUpdateQuery(req.getId()),req,5 * 60);
        return map;
    }

    private void delMap(Map<String, BigDecimal> map, String message){
        String key = message.substring(0,message.indexOf(":")+1);
        String valueStr = message.substring(message.indexOf(":") + 1);
        if(!map.containsKey(key)){
            map.put(key,new BigDecimal(valueStr));
        }else{
            map.put(key,map.get(key).add(new BigDecimal(valueStr)));
        }
    }

    @Override
    public boolean delete(JSONObject jsonObject) {
        DatasourceCharge datasourceCharge = jsonObject.toJavaObject(DatasourceCharge.class);
        //查询apiId是否有效
        DatasourceCharge selectOne = datasourceChargeMapper.selectByPrimaryKey(datasourceCharge.getId());
        CompanyAppsAuth queryAuth = new CompanyAppsAuth();
        queryAuth.setApiId(selectOne.getApiId());
        queryAuth.setStatus(1);
        //有效绑定list
        List<CompanyAppsAuth> list = companyAppsAuthMapper.listBySome(queryAuth);
        if(list!=null && list.size()>0){
            throw new BusinessException("该上游已有绑定appKey，请勿删除");
        }

        datasourceCharge.setStatus(0);

        int i = datasourceChargeMapper.updateByPrimaryKeySelective(datasourceCharge);
        if (i == 1) {
            RedisUtil.delete("datasource_charge:"+datasourceCharge.getApiId());
        }
        return i==1;
    }

    @Override
    public DatasourceCharge getChargeByApiId(JSONObject jsonObject) {
        DatasourceCharge datasourceCharge = jsonObject.toJavaObject(DatasourceCharge.class);
        Example example = new Example(DatasourceCharge.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status",1)
                .andEqualTo("apiId",datasourceCharge.getApiId());
        List<DatasourceCharge> datasourceChargeList = datasourceChargeMapper.selectByExample(example);
        if (datasourceChargeList != null && datasourceChargeList.size() > 0) {
            return datasourceChargeList.get(0);
        }

        return null;
    }

    @Override
//    @Cacheable(value = "getChargeByApiId", key = "'datasource_charge:' + #apiId", condition = "#result != null ")
    public DatasourceCharge getChargeByApiId(final Integer apiId) {
        Example example = new Example(DatasourceCharge.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status",1)
                .andEqualTo("apiId",apiId);
        List<DatasourceCharge> datasourceChargeList = datasourceChargeMapper.selectByExample(example);
        if (datasourceChargeList != null && datasourceChargeList.size() > 0) {
            return datasourceChargeList.get(0);
        }

        return null;
    }

    @Override
    public List<DatasourceCharge> getDatasourceChargeByBillingRulesUuid(String strategyUuid) {
        Example example = new Example(DatasourceCharge.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status",1)
                .andEqualTo("billingRulesUuid",strategyUuid);
        return datasourceChargeMapper.selectByExample(example);
    }

    /**
     * 校验业务对象唯一性
     *
     * @param datasourceCharge 对象
     * @throws Exception 异常
     */
    private void checkUnique(DatasourceCharge datasourceCharge){
        Example example = new Example(DatasourceCharge.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status",1)
                .andEqualTo("apiId",datasourceCharge.getApiId());
        long count = PageHelper.count(() -> datasourceChargeMapper.selectByExample(example));
        if (count != 0) {
            throw new BusinessException("api 已存在");
        }
    }
}
