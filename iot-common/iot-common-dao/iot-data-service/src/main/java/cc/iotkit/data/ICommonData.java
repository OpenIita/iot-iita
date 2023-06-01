/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.model.Id;
import cc.iotkit.common.api.Paging;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 数据基础服务接口
 */
public interface ICommonData<T extends Id<ID>, ID> {

    /**
     * 通过ID取数据
     */
    default T findById(ID id) {
        return null;

    }

    /**
     * 通过ID取数据
     */
    default List<T> findByIds(Collection<ID> id) {
        return Collections.EMPTY_LIST;

    }


    /**
     * 保存数据，id不为空更新，否则添加
     */
    default T save(T data) {
        return data;

    }


    /**
     * 批量保存数据
     */
    default void batchSave(List<T> data) {
    }

    /**
     * 按id删除
     */
    default void deleteById(ID id) {
    }

    /**
     * 按id批量删除
     */
    default void deleteByIds(Collection<ID> ids) {
    }

    /**
     * 总数统计
     */
    default long count() {
        return 0L;
    }

    /**
     * 取所有数据
     */
    default List<T> findAll() {
        return null;
    }

    /**
     * 分页获取所有信息
     */
    default Paging<T> findAll(PageRequest<T> pageRequest) {
        return null;
    }

    /**
     * 按条件查询多个结果
     */
    default List<T> findAllByCondition(T data) {
        return Collections.EMPTY_LIST;

    }

    /**
     * 按条件查询单个结果
     */
    default T findOneByCondition(T data) {
        return data;
    }
}
