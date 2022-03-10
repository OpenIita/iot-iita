package cc.iotkit.protocol;


/**
 * 设备行为接口
 */
public interface DeviceBehaviour {

    /**
     * 设备注册
     */
    Result register(RegisterInfo info);

    /**
     * 设备注销
     */
    Result deregister(DeregisterInfo info);

    /**
     * 设备上线
     */
    Result online(String productKey, String deviceName);

    /**
     * 设备离线
     */
    Result offline(String productKey, String deviceName);

    /**
     * 设备消息上报
     */
    void messageReport(DeviceMessage msg);

    /**
     * OTA消息上报
     */
    void otaProgressReport(OtaMessage msg);


}
