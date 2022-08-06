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

import cc.iotkit.common.Constants;
import cc.iotkit.data.IDeviceInfoData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.mq.ConsumerHandler;
import cc.iotkit.mq.MqConsumer;
import cc.iotkit.mq.MqProducer;
import cc.iotkit.temporal.IThingModelMessageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class DeviceMessageConsumer implements ConsumerHandler<ThingModelMessage> {
    @Lazy
    @Autowired
    private IThingModelMessageData thingModelMessageData;
    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;
    @Autowired
    private MqConsumer<ThingModelMessage> thingModelMessageConsumer;
    @Autowired
    private MqProducer<ThingModelMessage> thingModelMessageMqProducer;

    @PostConstruct
    public void init() {
        thingModelMessageConsumer.consume(Constants.THING_MODEL_MESSAGE_TOPIC, this);
    }

    @Override
    public void handler(ThingModelMessage msg) {
        try {
            String type = msg.getType();
            //重新发布属性入库消息
            if (ThingModelMessage.TYPE_PROPERTY.equals(type)
                    && "report".equals(msg.getIdentifier())) {
                thingModelMessageMqProducer.publish(Constants.DEVICE_PROPERTY_REPORT_TOPIC, msg);
            }
            if (ThingModelMessage.TYPE_CONFIG.equals(type)) {
                //重新发布设备配置消息，用于设备配置下发
                thingModelMessageMqProducer.publish(Constants.DEVICE_CONFIG_TOPIC, msg);
            }

            DeviceInfo device = deviceInfoData.findByDeviceId(msg.getDeviceId());
            if (device == null) {
                return;
            }
            msg.setUid(device.getUid());

            //设备消息入库
            thingModelMessageData.add(msg);
        } catch (Throwable e) {
            //不能重复消费
            log.error("device message consumer error", e);
        }
    }

}
