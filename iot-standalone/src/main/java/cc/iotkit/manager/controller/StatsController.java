/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller;

import cc.iotkit.data.ICategoryData;
import cc.iotkit.data.IDeviceInfoData;
import cc.iotkit.data.IProductData;
import cc.iotkit.manager.model.stats.MainStats;
import cc.iotkit.temporal.IThingModelMessageData;
import cc.iotkit.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/main")
    public MainStats getMainStats() {
        MainStats mainStats = new MainStats();
        String uid = AuthUtil.getUserId();

        long now = System.currentTimeMillis();
        if (AuthUtil.isAdmin()) {
            mainStats.setCategoryTotal(ICategoryData.count());
            mainStats.setProductTotal(productData.count());
            mainStats.setDeviceTotal(deviceInfoData.count());
            mainStats.setReportTotal(thingModelMessageData.count());
            //上报数据统计
            mainStats.setReportDataStats(thingModelMessageData.getDeviceMessageStatsWithUid(null, now - 48 * 3600 * 1000, now));
            //产品数量统计
            mainStats.setDeviceStatsOfCategory(deviceInfoData.getDeviceStatsByCategory(""));
        } else {
            mainStats.setCategoryTotal(ICategoryData.count());
            mainStats.setProductTotal(productData.countByUid(uid));
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
