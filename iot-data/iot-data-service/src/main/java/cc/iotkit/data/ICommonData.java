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

import cc.iotkit.model.Id;
import cc.iotkit.model.Paging;

import java.util.List;

/**
 * 数据基础服务接口
 */
public interface ICommonData<T extends Id<ID>, ID> {

    /**
     * 通过ID取数据
     */
    T findById(ID id);

    /**
     * 保存数据，id不为空更新，否则添加
     */
    T save(T data);

    /**
     * 添加数据
     */
    T add(T data);

    /**
     * 按id删除
     */
    void deleteById(ID id);

    /**
     * 总数统计
     */
    long count();

    /**
     * 取所有数据
     */
    List<T> findAll();

    /**
     * 分页获取所有信息
     *
     * @param page 页码，从0开始
     * @param size 分页大小
     */
    Paging<T> findAll(int page, int size);
}
