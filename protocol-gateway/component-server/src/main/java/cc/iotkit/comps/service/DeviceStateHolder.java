package cc.iotkit.comps.service;

import cc.iotkit.common.utils.ThreadUtil;
import cc.iotkit.comps.config.ServerConfig;
import cc.iotkit.dao.DeviceRepository;
import cc.iotkit.model.device.DeviceInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 设备状态维持，每1分钟更新一次心跳
 */
@Slf4j
//@Service
public class DeviceStateHolder implements MessageListener<DeviceStateHolder.OfflineMessage> {

    private ScheduledThreadPoolExecutor stateHolderTask;

    private Set<String> devices = new TreeSet<>();

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private DeviceRepository deviceRepository;

    private Producer<OfflineMessage> offlineMessageProducer;

    @PostConstruct
    public void init() throws PulsarClientException {
        stateHolderTask = ThreadUtil.newScheduled(4, "thread-device-state-holder");
        stateHolderTask.scheduleAtFixedRate(this::hold, 0, 1, TimeUnit.MINUTES);

        PulsarClient client = PulsarClient.builder()
                .serviceUrl(this.serverConfig.getPulsarBrokerUrl())
                .build();

        offlineMessageProducer = client.newProducer(Schema.JSON(OfflineMessage.class))
                .topic("persistent://iotkit/default/holder_offline")
                .create();

        client.newConsumer(Schema.JSON(OfflineMessage.class))
                .topic("persistent://iotkit/default/holder_offline")
                .subscriptionName("holder_offline")
                .consumerName("device-state-holder-consumer")
                .messageListener(this).subscribe();
    }

    public void online(String deviceId) {
        try {
            devices.add(deviceId);
            hold(deviceId);
            //上线后先产生离线消息
            offlineMessageProducer.send(new OfflineMessage(deviceId));
        } catch (Throwable e) {
            log.error("state holder online error", e);
        }
    }

    public void offline(String deviceId) {
        devices.remove(deviceId);
    }

    private void hold() {
        //标识在线
        for (String deviceId : devices) {
            hold(deviceId);
        }
    }

    private void hold(String deviceId) {
        redisTemplate.opsForValue().set("str:device:state:holder:" + deviceId,
                "1", 5, TimeUnit.SECONDS);
    }

    @SneakyThrows
    @Override
    public void received(Consumer<OfflineMessage> consumer, Message<OfflineMessage> msg) {
        String deviceId = msg.getValue().getDeviceId();
        //如果设备在线，不处理离线消息
        String hold = redisTemplate.opsForValue().get("str:device:state:holder:" + deviceId);
        if (hold != null) {
            return;
        }
        //如果设备不在线，则将设备更新为离线
        DeviceInfo device = deviceRepository.findByDeviceId(deviceId);
        DeviceInfo.State state = device.getState();
        state.setOnline(false);
        state.setOfflineTime(System.currentTimeMillis());
        deviceRepository.save(device);
        log.info("device offline,deviceId:{}", deviceId);

        consumer.acknowledge(msg);
    }

    @Override
    public void reachedEndOfTopic(Consumer<OfflineMessage> consumer) {

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfflineMessage {
        private String deviceId;
    }
}
