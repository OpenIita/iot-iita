/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.ruleengine.handler.sys;

import cc.iotkit.common.thing.ThingModelMessage;
import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.ruleengine.handler.DeviceMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * 设备状态检查
 */
@Slf4j
@Component
public class DeviceStateCheckHandler implements DeviceMessageHandler {

    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;


    @Override
    @Transactional
    public void handle(ThingModelMessage msg) {
        // 物模型->设备deviceName
        String deviceName = msg.getDeviceName();

        DeviceInfo realTimeDevice = deviceInfoData.findByDeviceName(deviceName);
        if ( !realTimeDevice.getState().isOnline() ) {
            // 切换状态，设备上线
            DeviceInfo.State state = realTimeDevice.getState();
            state.setOnline(true);
            state.setOnlineTime(System.currentTimeMillis());
            deviceInfoData.save(realTimeDevice);
        }
    }

}
