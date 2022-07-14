/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.dao;

import cc.iotkit.common.Constants;
import cc.iotkit.data.IDeviceInfoData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.stats.DataItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class DeviceCache {

    @Autowired
    private IDeviceInfoData deviceInfoData;
    @Autowired
    private DeviceDao deviceDao;

    private static DeviceCache INSTANCE;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public static DeviceCache getInstance() {
        return INSTANCE;
    }

    @Cacheable(value = Constants.DEVICE_CACHE, key = "#pk+'_'+#dn")
    public DeviceInfo getDeviceInfo(String pk, String dn) {
        return deviceInfoData.findByProductKeyAndDeviceName(pk, dn);
    }

    @Cacheable(value = Constants.DEVICE_CACHE, key = "#deviceId")
    public DeviceInfo get(String deviceId) {
        return deviceInfoData.findByDeviceId(deviceId);
    }

    @Cacheable(value = Constants.DEVICE_STATS_CACHE, key = "#uid")
    public List<DataItem> getDeviceStatsByCategory(String uid) {
        return deviceInfoData.getDeviceStatsByCategory(uid);
    }

}
