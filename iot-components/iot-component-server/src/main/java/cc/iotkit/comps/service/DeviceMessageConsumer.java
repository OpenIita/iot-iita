package cc.iotkit.comps.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.*;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.DeviceProperty;
import cc.iotkit.model.device.message.DeviceReport;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.mq.ConsumerHandler;
import cc.iotkit.mq.MqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class DeviceMessageConsumer implements ConsumerHandler<ThingModelMessage> {
    @Lazy
    @Autowired
    private ThingModelMessageRepository messageRepository;
    @Lazy
    @Autowired
    private DevicePropertyRepository propertyRepository;
    @Lazy
    @Autowired
    private DeviceReportRepository deviceReportRepository;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private DeviceCache deviceCache;
    @Autowired
    private MqConsumer<ThingModelMessage> thingModelMessageConsumer;

    @PostConstruct
    public void init() {
        thingModelMessageConsumer.consume(Constants.THING_MODEL_MESSAGE_TOPIC, this);
    }

    @Override
    public void handler(ThingModelMessage msg) {
        try {
            String deviceId = msg.getDeviceId();
            log.info("save message to es:{}", JsonUtil.toJsonString(msg));
            //属性入库
            if (ThingModelMessage.TYPE_PROPERTY.equals(msg.getType())
                    && "report".equals(msg.getIdentifier())) {
                log.info("update device property,deviceId:{},property:{}",
                        deviceId, JsonUtil.toJsonString(msg.getData()));
                deviceDao.updateProperties(deviceId, (Map<String, Object>) msg.getData());

                //设备属性历史数据存储
                if (msg.getData() instanceof Map) {
                    Map map = (Map) msg.getData();
                    int index = 0;
                    for (Object key : map.keySet()) {
                        index++;
                        try {
                            propertyRepository.save(
                                    new DeviceProperty(
                                            //防止重复id被覆盖
                                            msg.getMid() + "_" + index,
                                            deviceId,
                                            key.toString(),
                                            map.get(key),
                                            msg.getOccurred()
                                    )
                            );
                        } catch (Throwable e) {
                            log.warn("save property data to es error", e);
                        }
                    }
                }
            }

            try {
                //todo 存在性能问题，量大可再拆分处理
                //设备消息日志入库
                messageRepository.save(msg);
                //设备上报日志入库
                deviceReportRepository.save(getDeviceReport(msg));
            } catch (Throwable e) {
                log.warn("save device message to es error", e);
            }
        } catch (Throwable e) {
            //不能重复消费
            log.error("device message consumer error", e);
        }
    }

    private DeviceReport getDeviceReport(ThingModelMessage message) {
        DeviceInfo device = deviceCache.get(message.getDeviceId());
        return DeviceReport.builder()
                .id(UUID.randomUUID().toString())
                .deviceId(message.getDeviceId())
                .productKey(message.getProductKey())
                .deviceName(message.getDeviceName())
                .uid(device.getUid())
                .identifier(message.getIdentifier())
                .type(message.getType())
                .code(message.getCode())
                .time(message.getTime())
                .build();
    }
}
