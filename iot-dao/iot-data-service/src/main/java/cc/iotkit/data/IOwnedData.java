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

import cc.iotkit.common.api.Paging;
import cc.iotkit.model.Owned;

import java.util.Collections;
import java.util.List;

/**
 * 数据基础服务接口
 */
public interface IOwnedData<T extends Owned<ID>, ID> extends ICommonData<T, ID> {

    /**
     * 按所属用户取数据
     * @return
     */
    default List findByUid(String uid) {
        return Collections.emptyList();

    }

    default Paging<T> findByUid(String uid, int page, int size) {
        return null;
    }

    /**
     * 按所属用户统计总数
     */
    default long countByUid(String uid) {
        return 0L;

    }

}
