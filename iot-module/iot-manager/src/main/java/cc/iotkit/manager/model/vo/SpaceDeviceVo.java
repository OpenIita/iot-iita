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
package cc.iotkit.manager.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpaceDeviceVo {

    private Long id;

    /**
     * 空间中的设备id
     */
    private String deviceId;

    /**
     * 空间中的设备名称
     */
    private String name;

    /**
     * 设备DN
     */
    private String deviceName;

    /**
     * 设备图片
     */
    private String picUrl;

    /**
     * 空间ID
     */
    private Long spaceId;

    /**
     * 空间名称
     */
    private String spaceName;

    /**
     * 设备状态
     */
    private Boolean online;

    /**
     * 设备属性
     */
    private Map<String, Object> property = new HashMap<>();

    /**
     * 产品key
     */
    private String productKey;

    /**
     * 产品名
     */
    private String productName;

    /**
     * 品类
     */
    private String category;

    /**
     * 品类名
     */
    private String categoryName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否收藏
     */
    private Boolean collect;
}
