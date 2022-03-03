package cc.iotkit.server.handler;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.server.dao.DeviceEventRepository;
import cc.iotkit.server.dao.DeviceRepository;
import cc.iotkit.model.device.message.DeviceEvent;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.mq.Request;
import cc.iotkit.model.mq.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PropertyPostHandler implements MqttHandler<Map<String, Object>, Response.Empty> {

    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    DeviceEventRepository deviceEventRepository;

    @Override
    public boolean compliant(String topic) {
        return topic.endsWith("/event/property/post");
    }

    @Override
    public Request<Map<String, Object>> parse(String msg) {
        return JsonUtil.parse(msg, new TypeReference<Request<Map<String, Object>>>() {
        });
    }

    @Override
    public Response.Empty handler(String topic, DeviceInfo device, Request<Map<String, Object>> request) {
        device.setId(device.getDeviceId());
        if (device.getProperty() == null) {
            device.setProperty(new HashMap<>());
        }
        Map<String, Object> newProps = request.getParams();
        if (newProps != null && newProps.size() > 0) {
            request.getParams().forEach(device.getProperty()::put);
        }

        deviceRepository.save(device);

        DeviceEvent event = DeviceEvent.builder()
                .deviceId(device.getDeviceId())
                .identifier("propertyPost")
                .request(request)
                .type("property")
                .createAt(System.currentTimeMillis())
                .build();
        deviceEventRepository.save(event);
        return Response.empty();
    }

}
