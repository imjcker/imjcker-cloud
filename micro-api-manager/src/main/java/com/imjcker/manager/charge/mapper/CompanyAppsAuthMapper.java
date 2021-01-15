package com.imjcker.manager.charge.mapper;

import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.AuthParam;
import com.imjcker.manager.charge.po.CompanyAppsAuth;
import com.imjcker.manager.charge.po.CompanyAppsAuthVo;
import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.AuthParam;
import com.imjcker.manager.charge.po.CompanyAppsAuth;
import com.imjcker.manager.charge.po.CompanyAppsAuthVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 *
 * @author yzj_wangqw
 * @date 2020/04/09
 */
public interface CompanyAppsAuthMapper extends TkBaseMapper<CompanyAppsAuth> {

    List<CompanyAppsAuth> findAppKeyAndApiId(@Param("appKey") String appKey, @Param("apiId") Integer apiId, @Param("id") Integer id);

    @Update("update company_apps_auth set status=0 where id=#{id}")
    void deleteById(Integer id);

    @Update("update company_apps_auth set status=0 where api_id=#{apiId}")
    void deleteByApiId(Integer apiId);

    List<CompanyAppsAuthVo> queryAuth(AuthParam authParam);

    List<CompanyAppsAuth> selectAuthByStrategyUuid(@Param("strategyUuid") String strategyUuid);

    /**
     * @Description : 查询apiId是否
     * @Date : 2020/5/9 15:03
     * @Auth : qiuwen
     */
    List<CompanyAppsAuth> listBySome(CompanyAppsAuth companyAppsAuth);
}
