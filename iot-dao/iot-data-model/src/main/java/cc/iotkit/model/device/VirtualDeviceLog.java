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
package cc.iotkit.model.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 虚拟设备日志
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VirtualDeviceLog {

    private String id;

    /**
     * 虚拟设备id
     */
    private String virtualDeviceId;

    /**
     * 虚拟设备名称
     */
    private String virtualDeviceName;

    /**
     * 关联设备数量
     */
    private int deviceTotal;

    /**
     * 虚拟设备执行结果
     */
    private String result;

    /**
     * 创建时间
     */
    private Long logAt = System.currentTimeMillis();
}
