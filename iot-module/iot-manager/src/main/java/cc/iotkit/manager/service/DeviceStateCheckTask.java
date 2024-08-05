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
package cc.iotkit.manager.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.data.manager.IProductData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.Product;
import cc.iotkit.plugin.core.thing.IThingService;
import cc.iotkit.plugin.core.thing.actions.DeviceState;
import cc.iotkit.plugin.core.thing.actions.up.DeviceStateChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 设备状态检查定时任务
 */
@Slf4j
@Component
public class DeviceStateCheckTask {

    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;

    @Autowired
    @Qualifier("productDataCache")
    private IProductData productData;

    @Autowired
    private IThingService thingService;

    @Scheduled(fixedDelay = 10, initialDelay = 20, timeUnit = TimeUnit.SECONDS)
    public void syncState() {
        int pn = 1;
        Paging<DeviceInfo> all;
        while (true) {
            //取出数据库中所有在线设备
            all = deviceInfoData.findByConditions("","","","",true,"",pn,1000);
            //判断属性更新时间是否大于产品定义保活时长
            for (DeviceInfo device : all.getRows()) {
                Product product = productData.findByProductKey(device.getProductKey());
                Long keepAliveTime = product.getKeepAliveTime();
                if (keepAliveTime == null || keepAliveTime <= 0) {
                    continue;
                }
                String deviceId = device.getDeviceId();
                long lastTime = deviceInfoData.getLastTime(deviceId);
                //最后更新时间超时保活时长1.1倍认为设备离线了
                if (System.currentTimeMillis() - lastTime > keepAliveTime * 1000 * 1.1) {
                    DeviceInfo realTimeDevice = deviceInfoData.findByDeviceId(deviceId);
                    if (!realTimeDevice.isOnline()) {
                        continue;
                    }
                    log.info("device state check offline,{}", deviceId);

                    // 发送设备离线物模型消息
                    thingService.post("NONE", DeviceStateChange.builder()
                            .id(UniqueIdUtil.newRequestId())
                            .productKey(realTimeDevice.getProductKey())
                            .deviceName(realTimeDevice.getDeviceName())
                            .state(DeviceState.OFFLINE)
                            .time(System.currentTimeMillis())
                            .build());
                }
            }

            if (all.getRows().size() < 1000) {
                break;
            }
            pn++;
        }
    }

}
