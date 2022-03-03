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
public class EventReportHandler implements MqttHandler<Map<String, Object>, Response.Empty> {

    private static final Pattern MATCH_REG = Pattern.compile("^/sys/\\w+/\\w+/s/event/[^_/]+$");

    @Autowired
    DeviceEventRepository deviceEventRepository;

    @Override
    public boolean compliant(String topic) {
        return MATCH_REG.matcher(topic).matches();
    }

    @Override
    public Request parse(String msg) {
        return JsonUtil.parse(msg, new TypeReference<Request>() {
        });
    }

    @Override
    public Response.Empty handler(String topic, DeviceInfo device, Request request) {
        String identifier = topic.substring(topic.indexOf("/event/") + 7);
        DeviceEvent event = DeviceEvent.builder()
                .deviceId(device.getDeviceId())
                .identifier(identifier)
                .request(request)
                .type(topic.endsWith("_reply") ? "ack" : "event")
                .createAt(System.currentTimeMillis())
                .build();
        deviceEventRepository.save(event);
        return Response.empty();
    }

}
