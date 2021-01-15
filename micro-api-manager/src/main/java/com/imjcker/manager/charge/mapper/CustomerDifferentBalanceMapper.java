package com.imjcker.manager.charge.mapper;

import com.imjcker.manager.charge.po.CustomerDifferentBalance;
import com.imjcker.manager.charge.po.CustomerDifferentBalance;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 *
 * @author ztzh_wangtao
 * @date 2020/04/26
 */
public interface CustomerDifferentBalanceMapper extends BaseMapper<CustomerDifferentBalance> {

    Integer insertByList(@Param("list")List<CustomerDifferentBalance> list);
}
