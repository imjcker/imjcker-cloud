package com.imjcker.manager.charge.mapper;

import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.CompanyBalanceHistory;
import com.imjcker.manager.charge.vo.response.RespCompanyBalanceHistory;
import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.CompanyBalanceHistory;
import com.imjcker.manager.charge.vo.request.ReqSomeByPage;
import com.imjcker.manager.charge.vo.response.RespCompanyBalanceHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author yzj_wangqw
 * @date 2020/04/01
 */
public interface CompanyBalanceHistoryMapper extends TkBaseMapper<CompanyBalanceHistory> {

    /**
     * @Description : 分页查询 返回 resp
     * @Date : 2020/4/1 10:21
     * @Auth : qiuwen
     */
    List<RespCompanyBalanceHistory> listByAppKey(@Param("appKey")String appKey);

    RespCompanyBalanceHistory findNewestBalanceByAppKey(@Param("appKey") String appKey, @Param("timeStamp") Long timeStamp);

    List<RespCompanyBalanceHistory> listBySome(ReqSomeByPage reqSomeByPage);

    /**
     * @Description : 批量插入
     * @Date : 2020/5/20 17:27
     * @Auth : qiuwen
     */
    Integer insertByList(@Param("list") List<CompanyBalanceHistory> list);
}
