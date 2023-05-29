/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comps.service;

import cc.iotkit.common.constant.Constants;
import cc.iotkit.common.thing.ThingService;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.comps.DeviceComponentManager;
import cc.iotkit.data.manager.IDeviceConfigData;
import cc.iotkit.model.device.DeviceConfig;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.mq.ConsumerHandler;
import cc.iotkit.mq.MqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 设备配置服务
 */
@Slf4j
@Service
public class DeviceConfigConsumer implements ConsumerHandler<ThingModelMessage> {

    @Autowired
    private MqConsumer<ThingModelMessage> configMessageConsumer;

    @Autowired
    public DeviceComponentManager deviceComponentManager;

    @Autowired
    private IDeviceConfigData deviceConfigData;

    @PostConstruct
    public void init() {
        configMessageConsumer.consume(Constants.DEVICE_CONFIG_TOPIC, this);
    }

    @Override
    public void handler(ThingModelMessage msg) {
        try {
            String identifier = msg.getIdentifier();
            if (ThingModelMessage.ID_CONFIG_GET.equals(identifier)) {
                //收到设备获取配置消息，回复配置信息给设备
                DeviceConfig deviceConfig = deviceConfigData.findByDeviceId(msg.getDeviceId());
                if (deviceConfig == null) {
                    return;
                }

                Map config = JsonUtils.parseObject(deviceConfig.getConfig(), Map.class);
                ThingService<Object> service = ThingService.builder()
                        .productKey(msg.getProductKey())
                        .deviceName(msg.getDeviceName())
                        .identifier(ThingModelMessage.ID_CONFIG_GET + "_reply")
                        .type(ThingModelMessage.TYPE_CONFIG)
                        .mid(msg.getMid())
                        .params(config)
                        .build();
                deviceComponentManager.send(service);
            }
        } catch (Throwable e) {
            log.error("consumer device config msg error", e);
        }
    }
}
