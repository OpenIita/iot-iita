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

import cc.iotkit.model.Owned;
import cc.iotkit.model.Paging;

import java.util.List;

/**
 * 数据基础服务接口
 */
public interface IOwnedData<T extends Owned<ID>, ID> extends ICommonData<T, ID> {

    /**
     * 按所属用户取数据
     */
    List<T> findByUid(String uid);

    Paging<T> findByUid(String uid, int page, int size);

    /**
     * 按所属用户统计总数
     */
    long countByUid(String uid);

}
