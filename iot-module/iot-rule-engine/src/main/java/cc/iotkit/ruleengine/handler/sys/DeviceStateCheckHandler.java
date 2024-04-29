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
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.plugin.core.thing.actions.DeviceState;
import cc.iotkit.plugin.core.thing.actions.up.DeviceStateChange;
import cc.iotkit.ruleengine.handler.DeviceMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import cc.iotkit.plugin.core.thing.IThingService;

/**
 * 设备状态检查
 */
@Slf4j
@Component
public class DeviceStateCheckHandler implements DeviceMessageHandler {

    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;

    @Autowired
    private IThingService thingService;

    @Override
    public void handle(ThingModelMessage msg) {
        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(msg.getDeviceId());
        if (deviceInfo == null) {
            return;
        }

        String identifier = msg.getIdentifier();
        String type = msg.getType();

        //过滤下行消息
        if (ThingModelMessage.TYPE_PROPERTY.equals(type)) {
            if (ThingModelMessage.ID_PROPERTY_SET.equals(identifier)
                    || ThingModelMessage.ID_PROPERTY_GET.equals(identifier)
            ) {
                return;
            }
        }

        //过滤服务下发消息
        if (ThingModelMessage.TYPE_SERVICE.equals(type) && !identifier.endsWith("_reply")) {
            return;
        }

        //过滤oat消息
        if (ThingModelMessage.TYPE_CONFIG.equals(type) ||
                ThingModelMessage.TYPE_OTA.equals(type) ||
                ThingModelMessage.TYPE_LIFETIME.equals(type) ||
                ThingModelMessage.TYPE_STATE.equals(type)
        ) {
            return;
        }

        // 如果在线，则不处理
        if( deviceInfo.getState().isOnline() ) {
            return;
        }

        // 其他消息， 发送设备在线物模型消息
        thingService.post("NONE", DeviceStateChange.builder()
                .id(UniqueIdUtil.newRequestId())
                .productKey(deviceInfo.getProductKey())
                .deviceName(deviceInfo.getDeviceName())
                .state(DeviceState.ONLINE)
                .time(System.currentTimeMillis())
                .build());
    }

}
