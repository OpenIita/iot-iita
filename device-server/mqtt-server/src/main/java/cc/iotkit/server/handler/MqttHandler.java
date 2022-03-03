package cc.iotkit.server.handler;

import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.mq.Request;

public interface MqttHandler<T, R> {

    boolean compliant(String topic);

    Request<T> parse(String msg);

    R handler(String topic, DeviceInfo device, Request<T> request);

}
