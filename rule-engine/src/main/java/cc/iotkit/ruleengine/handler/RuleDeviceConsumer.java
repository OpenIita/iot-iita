package cc.iotkit.ruleengine.handler;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.model.device.message.ThingModelMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RuleDeviceConsumer implements MessageListener<ThingModelMessage> {

    private final List<DeviceMessageHandler> handlers = new ArrayList<>();

    @SneakyThrows
    public RuleDeviceConsumer(String broker, List<DeviceMessageHandler> handlers) {
        this.handlers.addAll(handlers);

        PulsarClient client = PulsarClient.builder()
                .serviceUrl(broker)
                .build();

        client.newConsumer(Schema.JSON(ThingModelMessage.class))
                .topic("persistent://iotkit/default/" + Constants.THING_MODEL_MESSAGE_TOPIC)
                .subscriptionName("rule-engine-device")
                .consumerName("rule-engine-device-consumer")
                .messageListener(this).subscribe();
    }

    @SneakyThrows
    @Override
    public void received(Consumer<ThingModelMessage> consumer, Message<ThingModelMessage> msg) {
        log.info("received thing model message:{}", JsonUtil.toJsonString(msg.getValue()));
        try {
            ThingModelMessage modelMessage = msg.getValue();
            for (DeviceMessageHandler handler : this.handlers) {
                handler.handle(modelMessage);
            }
        } catch (Throwable e) {
            log.error("rule device message process error", e);
        }
        consumer.acknowledge(msg);
    }

    @Override
    public void reachedEndOfTopic(Consumer<ThingModelMessage> consumer) {

    }

}
