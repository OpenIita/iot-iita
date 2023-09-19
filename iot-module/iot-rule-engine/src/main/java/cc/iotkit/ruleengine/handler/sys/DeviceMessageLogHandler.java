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

import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.ruleengine.handler.DeviceMessageHandler;
import cc.iotkit.temporal.IThingModelMessageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author sjg
 */
@Slf4j
@Component
public class DeviceMessageLogHandler implements DeviceMessageHandler {
    @Lazy
    @Autowired
    private IThingModelMessageData thingModelMessageData;
    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;

    @Override
    public void handle(ThingModelMessage msg) {
        DeviceInfo device = deviceInfoData.findByDeviceId(msg.getDeviceId());
        if (device == null) {
            return;
        }
        msg.setUid(device.getUid());

        //设备消息入库
        thingModelMessageData.add(msg);
    }

}
