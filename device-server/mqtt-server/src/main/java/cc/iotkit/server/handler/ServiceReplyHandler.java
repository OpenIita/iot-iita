package cc.iotkit.server.handler;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.server.dao.DeviceEventRepository;
import cc.iotkit.model.device.message.DeviceEvent;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.mq.Request;
import cc.iotkit.model.mq.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Pattern;

@Component
public class ServiceReplyHandler implements MqttHandler<Map<String, Object>, Response.Empty> {

    private static final Pattern MATCH_REG = Pattern.compile("^/sys/\\w+/\\w+/s/service/[^_/]+_reply$");

    @Autowired
    DeviceEventRepository deviceEventRepository;

    @Override
    public boolean compliant(String topic) {
        return MATCH_REG.matcher(topic).matches();
    }

    @Override
    public Request<Map<String, Object>> parse(String msg) {
        return JsonUtil.parse(msg, new TypeReference<Request<Map<String, Object>>>() {
        });
    }

    @Override
    public Response.Empty handler(String topic, DeviceInfo device, Request<Map<String, Object>> request) {
        String identifier = topic.substring(topic.indexOf("/service/") + 9);
        DeviceEvent event = DeviceEvent.builder()
                .deviceId(device.getDeviceId())
                .identifier(identifier)
                .request(request)
                .type("ack")
                .createAt(System.currentTimeMillis())
                .build();
        deviceEventRepository.save(event);
        return null;
    }

}
