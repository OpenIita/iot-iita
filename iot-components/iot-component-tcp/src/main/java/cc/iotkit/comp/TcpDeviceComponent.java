package cc.iotkit.comp;


import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.comp.tcp.cilent.TcpClientDeviceComponent;
import cc.iotkit.comp.tcp.server.TcpServerDeviceComponent;
import cc.iotkit.converter.DeviceMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author huangwenl
 * @date 2022-10-13
 */
@Slf4j
public class TcpDeviceComponent extends AbstractDeviceComponent {

    private AbstractDeviceComponent tcpVerticle;

    public void create(CompConfig config) {
        Map maps = JsonUtil.parse(config.getOther(), Map.class);
        String type = maps.get("type").toString();
        if ("server".equals(type)) {
            tcpVerticle = new TcpServerDeviceComponent();
        } else {
            tcpVerticle = new TcpClientDeviceComponent();
        }
        tcpVerticle.create(config);
    }

    @Override
    public void start() {
        tcpVerticle.start();
    }

    @Override
    public void stop() {
        tcpVerticle.stop();
    }

    @Override
    public void destroy() {
        tcpVerticle.destroy();
    }

    @Override
    public void onDeviceStateChange(DeviceState state) {
        tcpVerticle.onDeviceStateChange(state);
    }


    @Override
    public DeviceMessage send(DeviceMessage message) {
        return tcpVerticle.send(message);
    }
}
