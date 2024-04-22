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
                ThingModelMessage.TYPE_LIFETIME.equals(type)) {
            return;
        }

        //当前设备状态
        boolean online = false;
        //在离线消息
        if (ThingModelMessage.TYPE_STATE.equals(type)) {
            online = ThingModelMessage.ID_ONLINE.equals(identifier);
        } else {
            //其它消息都认作为在线
            online = true;
        }

        DeviceInfo.State state = deviceInfo.getState();
        if (state != null && state.isOnline() != online) {
            //状态有变更
            state.setOnline(online);
            if (online) {
                state.setOnlineTime(System.currentTimeMillis());
            } else {
                state.setOfflineTime(System.currentTimeMillis());
            }
        }
        deviceInfoData.save(deviceInfo);

    }

}
