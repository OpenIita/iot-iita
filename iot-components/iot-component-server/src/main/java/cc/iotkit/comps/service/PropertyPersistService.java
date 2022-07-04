package cc.iotkit.comps.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.DeviceDao;
import cc.iotkit.dao.DevicePropertyRepository;
import cc.iotkit.model.device.message.DeviceProperty;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.mq.ConsumerHandler;
import cc.iotkit.mq.MqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设备属性消息消费入库
 */
@Slf4j
@Service
public class PropertyPersistService implements ConsumerHandler<ThingModelMessage> {

    @Autowired
    private MqConsumer<ThingModelMessage> thingModelMessageMqConsumer;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private DevicePropertyRepository propertyRepository;

    @PostConstruct
    public void init() {
        thingModelMessageMqConsumer.consume(Constants.DEVICE_PROPERTY_REPORT_TOPIC, this);
    }

    @Override
    public void handler(ThingModelMessage msg) {
        if (!(msg.getData() instanceof Map)) {
            return;
        }

        Map<String, Object> properties = (Map<String, Object>) msg.getData();
        String deviceId = msg.getDeviceId();

        //更新设备当前属性
        updateDeviceCurrentProperties(deviceId, properties);

        //设备属性历史数据存储
        List<DeviceProperty> batch = new ArrayList<>();
        for (String key : properties.keySet()) {
            batch.add(new DeviceProperty(
                    //防止重复id被覆盖
                    msg.getMid() + "_" + key,
                    deviceId,
                    key,
                    properties.get(key),
                    msg.getOccurred()
            ));
        }

        //批量保存
        try {
            propertyRepository.saveAll(batch);
        } catch (Throwable e) {
            log.warn("save property data error", e);
        }
    }

    /**
     * 更新设备当前属性
     */
    private void updateDeviceCurrentProperties(String deviceId, Map<String, Object> properties) {
        try {
            log.info("update device property,deviceId:{},property:{}",
                    deviceId, JsonUtil.toJsonString(properties));
            deviceDao.updateProperties(deviceId, properties);
        } catch (Throwable e) {
            log.error("save device current properties error", e);
        }
    }
}
