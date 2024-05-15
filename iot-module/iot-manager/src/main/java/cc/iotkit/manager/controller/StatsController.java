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
package cc.iotkit.manager.controller;

import cc.iotkit.common.satoken.utils.AuthUtil;
import cc.iotkit.data.manager.ICategoryData;
import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.data.manager.IProductData;
import cc.iotkit.manager.model.stats.MainStats;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.temporal.IThingModelMessageData;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"状态"})
@Slf4j
@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private ICategoryData ICategoryData;
    @Autowired
    @Qualifier("productDataCache")
    private IProductData productData;
    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;
    @Autowired
    private IThingModelMessageData thingModelMessageData;

    @PostMapping("/main")
    public MainStats getMainStats() {
        MainStats mainStats = new MainStats();
        String uid = AuthUtil.getUserId();

        long now = System.currentTimeMillis();
        if (AuthUtil.isAdmin()) {
            mainStats.setCategoryTotal(ICategoryData.count());
            mainStats.setProductTotal(productData.count());
            mainStats.setDeviceTotal(deviceInfoData.count());

            DeviceInfo condition = new DeviceInfo();
            DeviceInfo.State state = new DeviceInfo.State();
            state.setOnline(true);
            condition.setState(state);
            mainStats.setOnlineTotal(deviceInfoData.findAllByCondition(condition).size());

            state.setOnline(false);
            condition.setState(state);
            mainStats.setOfflineTotal(deviceInfoData.findAllByCondition(condition).size());

            // 待激活设备
            mainStats.setNeverOnlineTotal(deviceInfoData.findNeverUsedDevices().size());

            mainStats.setReportTotal(thingModelMessageData.count());
            //上行数据统计
            mainStats.setDeviceUpMessageStats(thingModelMessageData.getDeviceUpMessageStatsWithUid(null, null, null));
            // 下行数据统计
            mainStats.setDeviceDownMessageStats(thingModelMessageData.getDeviceDownMessageStatsWithUid(null, null, null));
            //产品数量统计
            mainStats.setDeviceStatsOfCategory(deviceInfoData.getDeviceStatsByCategory(""));
        } else {
            mainStats.setCategoryTotal(ICategoryData.count());
//            mainStats.setProductTotal(productData.countByUid(uid));
            mainStats.setDeviceTotal(deviceInfoData.countByUid(uid));
//            mainStats.setReportTotal(deviceReportRepository.countByUid(uid));
            //上报数据统计
            mainStats.setReportDataStats(thingModelMessageData.getDeviceMessageStatsWithUid(uid, now - 48 * 3600 * 1000, now));
            //产品数量统计
            mainStats.setDeviceStatsOfCategory(deviceInfoData.getDeviceStatsByCategory(uid));
        }

        return mainStats;
    }

}
