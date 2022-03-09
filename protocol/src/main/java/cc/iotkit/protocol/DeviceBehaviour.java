package cc.iotkit.protocol;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 设备行为接口
 */
public interface DeviceBehaviour {

    /**
     * 设备注册
     */
    @PostMapping("/register")
    Result register(@RequestBody RegisterInfo info);

    /**
     * 设备注销
     */
    @PostMapping("/deregister")
    Result deregister(@RequestBody DeregisterInfo info);

    /**
     * 设备上线
     */
    @PostMapping("/online")
    void online(String productKey, String deviceName);

    /**
     * 设备离线
     */
    @PostMapping("/offline")
    void offline(String productKey, String deviceName);

    /**
     * 设备消息上报
     */
    void messageReport(DeviceMessage msg);

    /**
     * OTA消息上报
     */
    void otaProgressReport(OtaMessage msg);


}
