package cc.iotkit.comp.tcp.cilent;

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.comp.AbstractDeviceComponent;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.converter.DeviceMessage;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huangwenl
 * @date 2022-10-13
 */
@Slf4j
public class TcpClientDeviceComponent extends AbstractDeviceComponent {
    private Vertx vertx;
    private TcpClientVerticle tcpClientVerticle;
    private String deployedId;

    @Override
    public void create(CompConfig config) {
        super.create(config);
        vertx = Vertx.vertx();
        TcpClinetConfig tcpClinetConfig = JsonUtils.parseObject(config.getOther(), TcpClinetConfig.class);
        tcpClientVerticle = new TcpClientVerticle(tcpClinetConfig);
    }

    @Override
    public void start() {
        try {
            tcpClientVerticle.setExecutor(getHandler());
            Future<String> future = vertx.deployVerticle(tcpClientVerticle);
            future.onSuccess((s -> {
                        deployedId = s;
                        log.info("tcp client start success, deployId:{}", s);
                    }))
                    .onFailure((e -> {
                        log.error("tcp client start fail");
                        e.printStackTrace();
                    }));
            future.succeeded();
        } catch (Throwable e) {
            throw new BizException(ErrCode.COMPONENT_START_ERROR, e);
        }

    }

    @Override
    public void stop() {
        tcpClientVerticle.stop();
        Future<Void> future = vertx.undeploy(deployedId);
        future.onSuccess(unused -> log.info("stop tcpserver component success"));
    }

    @Override
    public void destroy() {
    }

    @Override
    public void onDeviceStateChange(DeviceState state) {
        if (DeviceState.STATE_OFFLINE.equals(state.getState())) {
            tcpClientVerticle.offlineDevice(state.getDeviceName());
        }
    }


    @Override
    public DeviceMessage send(DeviceMessage message) {
        tcpClientVerticle.sendMsg(message);
        return message;
    }
}
