package cc.iotkit.server.handler;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.DeviceRegister;
import cc.iotkit.model.mq.Request;
import cc.iotkit.server.service.DeviceService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterHandler implements MqttHandler<DeviceRegister, DeviceRegister> {

    @Autowired
    private DeviceService deviceService;

    @Override
    public boolean compliant(String topic) {
        return topic.endsWith("/register");
    }

    @Override
    public Request<DeviceRegister> parse(String msg) {
        return JsonUtil.parse(msg, new TypeReference<Request<DeviceRegister>>() {
        });
    }

    @Override
    public DeviceRegister handler(String topic, DeviceInfo device, Request<DeviceRegister> request) {
        DeviceRegister regInfo = request.getParams();
        deviceService.register(device.getDeviceId(), regInfo.getProductKey(),
                regInfo.getDeviceName(), regInfo.getModel());
        return regInfo;
    }
}
