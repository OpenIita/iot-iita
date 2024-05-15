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

import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.data.manager.IProductData;
import cc.iotkit.data.manager.ISpaceDeviceData;
import cc.iotkit.manager.model.vo.SpaceDeviceVo;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.space.SpaceDevice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpaceDeviceService {
    @Autowired
    private ISpaceDeviceData spaceDeviceData;
    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;
    @Autowired
    @Qualifier("productDataCache")
    private IProductData productData;
//TODO 没看到使用
//    public List<SpaceDeviceVo> getUserDevices(String uid, String spaceId) {
//        SpaceDevice device = new SpaceDevice();
//        device.setUid(uid);
//        List<SpaceDevice> spaceDevices;
//        if (StringUtils.isNotBlank(spaceId)) {
//            device.setSpaceId(spaceId);
//            spaceDevices = spaceDeviceData.findByUidAndSpaceIdOrderByAddAtDesc(uid, spaceId);
//        } else {
//            spaceDevices = spaceDeviceData.findBySpaceIdOrderByAddAtDesc(spaceId);
//        }
//
//        List<SpaceDeviceVo> spaceDeviceVos = new ArrayList<>();
//        spaceDevices.forEach(sd -> {
//            DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(sd.getDeviceId());
//            Product product = productData.findByProductKey(deviceInfo.getProductKey());
//            spaceDeviceVos.add(SpaceDeviceVo.builder()
//                    .uid(sd.getUid())
//                    .deviceId(sd.getDeviceId())
//                    .name(sd.getName())
//                    .picUrl(product.getImg())
//                    .online(deviceInfo.getState().isOnline())
//                    .property(deviceInfo.getProperty())
//                    .productKey(deviceInfo.getProductKey())
//                    .build());
//        });
//        return spaceDeviceVos;
//    }
}
