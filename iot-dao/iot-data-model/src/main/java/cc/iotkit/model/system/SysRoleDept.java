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

package cc.iotkit.model.system;

import cc.iotkit.model.BaseModel;
import cc.iotkit.model.Id;
import cc.iotkit.model.TenantModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * author: 石恒
 * date: 2023-05-30 16:16
 * description:
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleDept extends TenantModel implements Id<Long>, Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;
}
