package com.imjcker.manager.charge.mapper;

import com.imjcker.manager.charge.po.AuthDifferentStock;
import com.imjcker.manager.charge.po.AuthDifferentStock;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 *
 * @author ztzh_wangtao
 * @date 2020/04/26
 */
public interface AuthDifferentStockMapper extends BaseMapper<AuthDifferentStock> {

    Integer insertByList(@Param("list")List<AuthDifferentStock> list);
}
