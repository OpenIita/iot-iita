/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */
package cc.iotkit.data;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.Id;

import java.util.Collection;
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
     * 通过ID取数据
     */
     List<T> findByIds(Collection<ID> id);


    /**
     * 保存数据，id不为空更新，否则添加
     */
     T save(T data) ;


    /**
     * 批量保存数据
     */
     void batchSave(List<T> data) ;
    /**
     * 按id删除
     */
     void deleteById(ID id) ;
    /**
     * 按id批量删除
     */
     void deleteByIds(Collection<ID> ids) ;
    /**
     * 总数统计
     */
     long count();
    /**
     * 取所有数据
     */
     List<T> findAll() ;

    /**
     * 分页获取所有信息
     */
     Paging<T> findAll(PageRequest<T> pageRequest) ;

    /**
     * 按条件查询多个结果
     */
     List<T> findAllByCondition(T data);

    /**
     * 按条件查询单个结果
     */
     T findOneByCondition(T data);
}
