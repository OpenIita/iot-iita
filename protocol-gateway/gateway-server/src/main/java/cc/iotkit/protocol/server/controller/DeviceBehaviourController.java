package cc.iotkit.protocol.server.controller;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.protocol.*;
import cc.iotkit.protocol.client.DeviceBehaviourClient;
import cc.iotkit.protocol.server.config.ServerConfig;
import cc.iotkit.protocol.server.service.DeviceBehaviourService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/device_behaviour")
public class DeviceBehaviourController implements DeviceBehaviour {

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private DeviceBehaviourService behaviourService;


    @Override
    @PostMapping("/register")
    public Result register(@RequestBody RegisterInfo info) {
        return behaviourService.register(info);
    }

    @Override
    @PostMapping("/online")
    public Result online(String productKey, String deviceName) {
        return behaviourService.deviceStateChange(productKey, deviceName, true);
    }

    @Override
    @PostMapping("/offline")
    public Result offline(String productKey, String deviceName) {
        return behaviourService.deviceStateChange(productKey, deviceName, false);
    }

    @PostMapping("/getConfig")
    public Result getConfig() {
        return new Result(true, JsonUtil.toJsonString(
                new DeviceBehaviourClient.GatewayConfig(serverConfig.getPulsarBrokerUrl())));
    }

    @Override
    public void messageReport(DeviceMessage msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void otaProgressReport(OtaMessage msg) {
        throw new UnsupportedOperationException();
    }


}
