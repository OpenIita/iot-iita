package cc.iotkit.deviceapi;

import cc.iotkit.model.device.DeviceInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Component
@FeignClient(value = "iot-device-manager",url = "localhost:8091")
public interface IDeviceManager {

    /**
     * 设备注册
     */
    @PostMapping("/register")
    @ResponseBody
    DeviceInfo register(@RequestParam("parentId") String parentId,
                        @RequestParam("productKey") String productKey,
                        @RequestParam("deviceName") String deviceName,
                        @RequestParam("model") String model);

    /**
     * 解绑子设备
     */
    @PostMapping("/{deviceId}/unbind")
    void unbind(@PathVariable("deviceId") String deviceId);

    /**
     * 设置属性
     */
    @PostMapping("/{deviceId}/property/set")
    String setProperty(@PathVariable("deviceId") String deviceId,
                       @RequestBody Map<String, Object> properties);

    /**
     * 调用服务
     */
    @PostMapping("/{deviceId}/{identifier}/invoke")
    String invokeService(@PathVariable("deviceId") String deviceId,
                         @PathVariable("identifier") String identifier,
                         @RequestBody Map<String, Object> properties);
}
