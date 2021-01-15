package com.imjcker.manager.charge.mapper;

import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.CompanyApps;
import com.imjcker.manager.charge.po.CompanyAppsVo;
import com.imjcker.manager.charge.vo.response.RespCompanyKey;
import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.CompanyApps;
import com.imjcker.manager.charge.po.CompanyAppsVo;
import com.imjcker.manager.charge.vo.response.RespCompanyKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author yzj_wangqw
 * @date 2020/04/09
 */
public interface CompanyAppsMapper extends TkBaseMapper<CompanyApps> {
    List<CompanyAppsVo> queryCustomer(@Param("appKey") String appKey);

    void update(@Param("app") CompanyApps app);

    void addCustomer(@Param("app") CompanyApps app);

    void deleteByAppKey(@Param("appKey") String appKey);

    CompanyAppsVo findCustomerByAppKey(@Param("appKey") String appKey);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appKey", column = "app_key"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "statusFlag", column = "status_flag"),
            @Result(property = "description", column = "description"),
            @Result(property = "balance", column = "balance"),
            @Result(property = "strategyUuid", column = "strategy_uuid"),
            @Result(property = "stock", column = "stock"),
            @Result(property = "price", column = "price")
    })
    @Select("select * from company_apps where status_flag = 1 and strategy_uuid = #{strategyUuid}")
    List<CompanyApps> selectCompanyByStrategyUuid(@Param("strategyUuid") String strategyUuid);

    /**
     * 查询公钥 私钥字段
     * @param appKey
     * @return
     */
    RespCompanyKey selectKey(@Param("appKey") String appKey);

    /**
     * @Description : 查appKey list
     * @Date : 2020/5/13 15:29
     * @Auth : qiuwen
     */
    List<CompanyApps> listByAppKeys(@Param("list")List<String> list);
}
