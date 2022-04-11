package cc.iotkit.comps.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comps.config.ServerConfig;
import cc.iotkit.dao.DeviceDao;
import cc.iotkit.dao.DevicePropertyRepository;
import cc.iotkit.dao.ThingModelMessageRepository;
import cc.iotkit.model.device.message.DeviceProperty;
import cc.iotkit.model.device.message.ThingModelMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Slf4j
@Service
public class DeviceMessageConsumer implements MessageListener<ThingModelMessage> {

    @Autowired
    private ServerConfig serverConfig;
    @Lazy
    @Autowired
    private ThingModelMessageRepository messageRepository;
    @Lazy
    @Autowired
    private DevicePropertyRepository propertyRepository;
    @Autowired
    private DeviceDao deviceDao;

    @PostConstruct
    public void init() throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(this.serverConfig.getPulsarBrokerUrl())
                .build();

        client.newConsumer(Schema.JSON(ThingModelMessage.class))
                .topic("persistent://iotkit/default/" + Constants.THING_MODEL_MESSAGE_TOPIC)
                .subscriptionName("thing-model-message")
                .consumerName("thing-model-message-consumer")
                .messageListener(this).subscribe();
    }

    @SneakyThrows
    @Override
    public void received(Consumer<ThingModelMessage> consumer, Message<ThingModelMessage> msg) {
        try {
            ThingModelMessage modelMessage = msg.getValue();
            String deviceId = modelMessage.getDeviceId();
            log.info("save message to es:{}", JsonUtil.toJsonString(modelMessage));
            //属性入库
            if (ThingModelMessage.TYPE_PROPERTY.equals(modelMessage.getType())
                    && "report".equals(modelMessage.getIdentifier())) {
                log.info("update device property,deviceId:{},property:{}",
                        deviceId, JsonUtil.toJsonString(modelMessage.getData()));
                deviceDao.updateProperties(deviceId, (Map<String, Object>) modelMessage.getData());

                //设备属性历史数据存储
                if (modelMessage.getData() instanceof Map) {
                    Map map = (Map) modelMessage.getData();
                    int index = 0;
                    for (Object key : map.keySet()) {
                        index++;
                        try {
                            propertyRepository.save(
                                    new DeviceProperty(
                                            //防止重复id被覆盖
                                            modelMessage.getMid() + "_" + index,
                                            deviceId,
                                            key.toString(),
                                            map.get(key),
                                            modelMessage.getOccurred()
                                    )
                            );
                        } catch (Throwable e) {
                            log.warn("save property data to es error", e);
                        }
                    }
                }
            }

            try {
                //设备消息日志入库
                messageRepository.save(modelMessage);
            } catch (Throwable e) {
                log.warn("save device message to es error", e);
            }
        } catch (Throwable e) {
            //不能重复消费
            log.error("device message consumer error", e);
        }
        consumer.acknowledge(msg);
    }

    @Override
    public void reachedEndOfTopic(Consumer<ThingModelMessage> consumer) {

    }

}
