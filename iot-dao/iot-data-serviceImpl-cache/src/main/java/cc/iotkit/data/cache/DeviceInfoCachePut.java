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
package cc.iotkit.data.cache;

import cc.iotkit.common.constant.Constants;
import cc.iotkit.model.device.DeviceInfo;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeviceInfoCachePut {

    @CachePut(value = Constants.CACHE_DEVICE_INFO, key = "#root.method.name+#deviceId")
    public DeviceInfo findByDeviceId(String deviceId, DeviceInfo deviceInfo) {
        return deviceInfo;
    }

    @CachePut(value = Constants.CACHE_DEVICE_INFO, key = "#root.method.name+#deviceName")
    public DeviceInfo findByDeviceName(String deviceName, DeviceInfo deviceInfo) {
        return deviceInfo;
    }

    @CachePut(value = Constants.CACHE_DEVICE_INFO, key = "#root.method.name+#parentId")
    public List<String> findSubDeviceIds(String parentId, List<String> subDeviceIds) {
        return subDeviceIds;
    }

}
