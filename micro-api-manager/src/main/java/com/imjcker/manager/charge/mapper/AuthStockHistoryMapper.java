package com.imjcker.manager.charge.mapper;

import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.AuthStockHistory;
import com.imjcker.manager.charge.vo.response.RespAuthStockHistory;
import com.imjcker.manager.charge.base.TkBaseMapper;
import com.imjcker.manager.charge.po.AuthStockHistory;
import com.imjcker.manager.charge.vo.request.ReqStockByPage;
import com.imjcker.manager.charge.vo.response.RespAuthStockHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author yzj_wangqw
 * @date 2020/04/01
 */
public interface AuthStockHistoryMapper extends TkBaseMapper<AuthStockHistory> {

    /**
     * @Description : 返回resp对象
     * @Date : 2020/4/1 10:56
     * @Auth : qiuwen
     */
    List<RespAuthStockHistory> listForPage(ReqStockByPage req);

    RespAuthStockHistory findNewestAuthStock(@Param("appKey") String appKey,
                                             @Param("apiId") Integer apiId, @Param("timeStamp") Long timeStamp);

    Integer inserByList(@Param("list") List<AuthStockHistory> list);
}
