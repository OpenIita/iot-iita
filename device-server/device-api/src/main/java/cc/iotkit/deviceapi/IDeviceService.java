package cc.iotkit.deviceapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(value = "iot-device-service", url = "localhost:8091")
public interface IDeviceService {

    /**
     * 调用服务
     */
    @PostMapping("/invoke")
    String invoke(@RequestBody Service service);
}
