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
package cc.iotkit.model.product;

import cc.iotkit.model.Id;
import cc.iotkit.model.TenantModel;
import lombok.*;

import java.io.Serializable;

/**
 * @Author: tfd
 * @Date: 2024/4/25 14:32
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Icon extends TenantModel implements Id<Long>, Serializable {

    private Long id;

    private Long iconTypeId;

    private String iconName;

    private String viewBox;

    private String xmlns;

    private String version;

    private String iconContent;
}