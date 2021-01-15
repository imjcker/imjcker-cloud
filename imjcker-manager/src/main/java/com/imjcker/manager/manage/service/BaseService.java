package com.imjcker.manager.manage.service;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * common methods for service
 * @author thh
 * @param <T>
 */
public interface BaseService<T> {
    /**
     * 数据插入
     * @param obj 保存对象
     * @return 操作结果
     */
    boolean insert(T obj);

    /**
     * 保存数据
     * @param obj 保存对象
     * @return 操作结果
     */
    boolean save(T obj);

    /**
     * 更新数据
     * @param obj 更新对象
     * @return 更新结果
     */
    boolean update(T obj);

    /**
     * 删除数据
     * @param obj 删除对象
     * @return 删除结果
     */
    boolean delete(T obj);

    /**
     * 通过主键查询对象
     * @param id 主键ID
     * @return 存在返回对象,不存在返回null
     */
    T findById(Integer id);

    /**
     * 查询所有
     * @return 查询结果集合或null
     */
    List<T> findAll();

    /**
     * 分页查询
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @param obj 查询条件的对象
     * @return 分页信息
     */
    PageInfo<T> findForPage(Integer pageNum, Integer pageSize, T obj);

    /**
     * 分页查询
     * @param obj 查询条件对象,包含pageNum,pageSize字段
     * @return 分页信息
     */
    PageInfo<T> findForPage(T obj);

}
