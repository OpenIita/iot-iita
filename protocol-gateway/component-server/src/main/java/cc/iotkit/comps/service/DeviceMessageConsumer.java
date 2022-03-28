package cc.iotkit.comps.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comps.config.ServerConfig;
import cc.iotkit.dao.DeviceDao;
import cc.iotkit.dao.DevicePropertyRepository;
import cc.iotkit.dao.ThingModelMessageRepository;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.device.message.DeviceProperty;
import cc.iotkit.model.device.message.ThingModelMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceMessageConsumer implements MessageListener<ThingModelMessage> {

    private final ServerConfig serverConfig;
    private final ThingModelMessageRepository messageRepository;
    private final DevicePropertyRepository propertyRepository;
    private final DeviceDao deviceDao;

    @SneakyThrows
    @Autowired
    public DeviceMessageConsumer(ServerConfig serverConfig,
                                 ThingModelMessageRepository messageRepository,
                                 UserInfoRepository userInfoRepository,
                                 DevicePropertyRepository propertyRepository,
                                 DeviceDao deviceDao) {
        this.serverConfig = serverConfig;
        this.messageRepository = messageRepository;
        this.propertyRepository = propertyRepository;
        this.deviceDao = deviceDao;
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(this.serverConfig.getPulsarBrokerUrl())
                .build();

        String topicFormat = "persistent://%s/default/" + Constants.THING_MODEL_MESSAGE_TOPIC;
        List<UserInfo> platformUsers = userInfoRepository.findByType(UserInfo.USER_TYPE_PLATFORM);
        List<String> topics = platformUsers.stream().map(u -> String.format(topicFormat, u.getUid()))
                .collect(Collectors.toList());
        log.info("subscribe device_thing topic:{}", JsonUtil.toJsonString(topics));

        client.newConsumer(Schema.JSON(ThingModelMessage.class))
                .topics(topics)
                .subscriptionName("thing-model-message")
                .consumerName("thing-model-message-consumer")
                .messageListener(this).subscribe();
    }

    @SneakyThrows
    @Override
    public void received(Consumer<ThingModelMessage> consumer, Message<ThingModelMessage> msg) {
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
                for (Object key : map.keySet()) {
                    propertyRepository.save(
                            new DeviceProperty(
                                    modelMessage.getMid(),
                                    deviceId,
                                    key.toString(),
                                    map.get(key),
                                    modelMessage.getOccurred()
                            )
                    );
                }
            }
        }

        //设备消息日志入库
        messageRepository.save(modelMessage);
        consumer.acknowledge(msg);
    }

    @Override
    public void reachedEndOfTopic(Consumer<ThingModelMessage> consumer) {

    }

}
