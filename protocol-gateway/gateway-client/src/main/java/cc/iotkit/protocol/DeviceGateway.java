package cc.iotkit.protocol;


import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 设备网关接口
 */
public interface DeviceGateway {

    /**
     * 指令下发
     */
    @RequestMapping("/sendMessage")
    Result sendMessage(DeviceMessage msg);

    /**
     * OTA升级任务下发
     */
    @RequestMapping("/sendOta")
    Result sendOta(OtaInfo ota);

}
