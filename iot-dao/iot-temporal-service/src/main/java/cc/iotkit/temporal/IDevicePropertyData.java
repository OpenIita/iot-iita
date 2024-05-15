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
package cc.iotkit.temporal;


import cc.iotkit.model.device.message.DeviceProperty;
import cc.iotkit.model.device.message.DevicePropertyCache;

import java.util.List;
import java.util.Map;

/**
 * 设备属性时序数据接口
 */
public interface IDevicePropertyData {

    /**
     * 按时间范围取设备指定属性的历史数据
     *
     * @param deviceId 设备id
     * @param name     属性名称
     * @param start    开始时间戳
     * @param end      结束时间戳
     * @param size     取时间范围内的数量
     */
    List<DeviceProperty> findDevicePropertyHistory(String deviceId, String name, long start, long end, int size);

    /**
     * 添加多个属性
     *
     * @param deviceId   设备ID
     * @param properties 属性
     * @param time       属性上报时间
     */
    void addProperties(String deviceId, Map<String, DevicePropertyCache> properties, long time);

}
