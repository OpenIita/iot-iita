package cc.iotkit.protocol.server.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.ThingModelMessageRepository;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.protocol.server.config.ServerConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceMessageConsumer implements MessageListener<ThingModelMessage> {

    private final ServerConfig serverConfig;

    private final ThingModelMessageRepository messageRepository;

    private final UserInfoRepository userInfoRepository;

    @SneakyThrows
    @Autowired
    public DeviceMessageConsumer(ServerConfig serverConfig,
                                 ThingModelMessageRepository messageRepository,
                                 UserInfoRepository userInfoRepository) {
        this.serverConfig = serverConfig;
        this.messageRepository = messageRepository;
        this.userInfoRepository = userInfoRepository;

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
        log.info("receive message:{}", JsonUtil.toJsonString(modelMessage));
        //设备消息日志入库
        messageRepository.save(modelMessage);

        messageRepository.findAll().forEach(m -> {
            log.info(JsonUtil.toJsonString(m));
        });

        consumer.acknowledge(msg);
    }

    @Override
    public void reachedEndOfTopic(Consumer<ThingModelMessage> consumer) {

    }

}
