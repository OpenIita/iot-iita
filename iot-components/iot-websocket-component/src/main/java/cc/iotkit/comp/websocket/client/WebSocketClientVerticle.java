package cc.iotkit.comp.websocket.client;

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.model.ReceiveResult;
import cc.iotkit.comp.model.RegisterInfo;
import cc.iotkit.comp.websocket.AbstractDeviceVerticle;
import cc.iotkit.converter.DeviceMessage;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketConnectOptions;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebSocketClientVerticle extends AbstractDeviceVerticle {

    private HttpClient httpClient;

    private WebSocket webSocketClient;

    private WebSocketClientConfig webSocketConfig;

    private long timerID;

    private final Map<String, Device> devices = new ConcurrentHashMap<>();

    public void setWebSocketClient(WebSocket webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    public WebSocketClientVerticle(String config) {
        this.webSocketConfig = JsonUtil.parse(config, WebSocketClientConfig.class);
    }

    public void start() {
        WebSocketConnectOptions options = new WebSocketConnectOptions().setPort(webSocketConfig.getPort())
                .setHost(webSocketConfig.getIp()).setURI(webSocketConfig.getUrl()).setSsl(webSocketConfig.isSsl());
        httpClient = vertx.createHttpClient();
        httpClient.webSocket(options).onSuccess(ws -> {
            setWebSocketClient(ws);
            log.info("webSocket client connect success!");
            ws.textMessageHandler(data -> {
                log.info("webSocket client receive msg:" + data);
                executor.onReceive(new HashMap<>(), null, data, (ret) -> {
                    if (ret != null && ret.getData() instanceof RegisterInfo) {
                        executor.onReceive(null, "connected", data, (r) -> {
                            if (!devices.containsKey(getDeviceKey(r))) {
                                devices.put(getDeviceKey(r), new Device(r.getDeviceName(), r.getProductKey()));
                            }
                        });
                    }
                });
            });
            ws.closeHandler(e -> {
                for (String deviceKey : devices.keySet()) {
                    executor.onReceive(null, "disconnect", deviceKey);
                }
                log.warn("client connection closed!");
            });
            ws.exceptionHandler(e -> {
                for (String deviceKey : devices.keySet()) {
                    executor.onReceive(null, "disconnect", deviceKey);
                }
                log.error("webSocket client connect exception!");
            });
            if (webSocketConfig.getHeartBeatTime() > 0 && StringUtils.isNotBlank(webSocketConfig.getHeartBeatData())) {
                timerID = vertx.setPeriodic(webSocketConfig.getHeartBeatTime(), t -> {
                    if (webSocketClient.isClosed()) {
                        vertx.cancelTimer(timerID);
                    }
                    executor.onReceive(new HashMap<>(), "ping", JsonUtil.toJsonString(webSocketConfig));
                });
            }
        }).onFailure(e -> {
            log.info("webSocket client connect failed!");
        });
    }

    @SneakyThrows
    public void stop() {
        vertx.cancelTimer(timerID);
        for (String deviceKey : devices.keySet()) {
            executor.onReceive(null, "disconnect", deviceKey);
        }
        httpClient.close();
    }

    @Override
    public DeviceMessage send(DeviceMessage message) {
        Object obj = message.getContent();
        if (!(obj instanceof Map)) {
            throw new BizException(ErrCode.DATA_FORMAT_ERROR);
        }
        String msgStr = JsonUtil.toJsonString(obj);
        log.info("send msg payload:{}", msgStr);
        Future<Void> result = webSocketClient.writeTextMessage(msgStr);
        result.onFailure(e -> log.error("webSocket client send msg failed", e));
        return message;
    }

    private String getDeviceKey(ReceiveResult result) {
        return getDeviceKey(result.getProductKey(), result.getDeviceName());
    }

    private String getDeviceKey(String productKey, String deviceName) {
        return String.format("%s_%s", productKey, deviceName);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Device {
        private String productKey;
        private String deviceName;
    }
}
