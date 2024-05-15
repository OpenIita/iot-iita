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

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends TenantModel implements Id<Long>, Serializable {

    //定位更新方式，手动/设备上报
    public static final String LOCATE_MANUAL = "manual";
    public static final String LOCATE_DEVICE = "device";

    private Long id;

    private String productKey;

    private String productSecret;

    private String name;

    private String category;

    private Integer nodeType;

    /**
     * 所属平台用户ID
     */
    private String uid;

    private String img;

    private Long iconId;

    /**
     * 是否透传
     */
    private Boolean transparent;

    /**
     * 是否开启定位
     */
    private Boolean isOpenLocate;

    /**
     * 定位更新方式
     */
    private String locateUpdateType;

    /**
     * 保活时长（秒）
     */
    private Long keepAliveTime;

    private Long createAt;

    public boolean isTransparent() {
        return transparent != null && transparent;
    }
}