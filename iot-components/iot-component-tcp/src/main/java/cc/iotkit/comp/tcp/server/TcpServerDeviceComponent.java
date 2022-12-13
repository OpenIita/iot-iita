package cc.iotkit.comp.tcp.server;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
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
public class TcpServerDeviceComponent extends AbstractDeviceComponent {

    private Vertx vertx;
    private TcpServerVerticle tcpServerVerticle;
    private String deployedId;

    public void create(CompConfig config) {
        super.create(config);
        vertx = Vertx.vertx();
        TcpServerConfig serverConfig = JsonUtil.parse(config.getOther(), TcpServerConfig.class);
        tcpServerVerticle = new TcpServerVerticle(serverConfig);
    }

    @Override
    public void start() {
        try {
            tcpServerVerticle.setExecutor(getHandler());
            Future<String> future = vertx.deployVerticle(tcpServerVerticle);
            future.onSuccess((s -> {
                        deployedId = s;
                        log.info("tcp server start success, deployId:{}", s);
                    }))
                    .onFailure((e -> {
                        log.error("tcp server start fail");
                        e.printStackTrace();
                    }));
            future.succeeded();
        }catch (Throwable e){
            throw new BizException("start tcpserver component error", e);
        }

    }

    @Override
    public void stop() {
        tcpServerVerticle.stop();
        Future<Void> future = vertx.undeploy(deployedId);
        future.onSuccess(unused -> log.info("stop tcpserver component success"));
    }

    @Override
    public void destroy() {
    }

    @Override
    public void onDeviceStateChange(DeviceState state) {
        if (DeviceState.STATE_OFFLINE.equals(state.getState())){
            tcpServerVerticle.offlineDevice(state.getDeviceName());
        }else if(DeviceState.STATE_ONLINE.equals(state.getState())){
            tcpServerVerticle.onlineDevice(state.getDeviceName(),
                    state.getParent() != null ? state.getParent().getDeviceName() : null);
        }
    }


    @Override
    public DeviceMessage send(DeviceMessage message) {
        tcpServerVerticle.sendMsg(message);
        return message;
    }
}
