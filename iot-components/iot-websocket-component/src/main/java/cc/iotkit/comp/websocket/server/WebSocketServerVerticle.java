package cc.iotkit.comp.websocket.server;


import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.model.ReceiveResult;
import cc.iotkit.comp.websocket.AbstractDeviceVerticle;
import cc.iotkit.converter.DeviceMessage;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.net.PemKeyCertOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class WebSocketServerVerticle extends AbstractDeviceVerticle {


    private HttpServer httpServer;

    private final WebSocketServerConfig webSocketConfig;

    private final Map<String, ServerWebSocket> wsClients = new ConcurrentHashMap<>();

    public WebSocketServerVerticle(String config) {
        this.webSocketConfig = JsonUtil.parse(config, WebSocketServerConfig.class);
    }

    private final Map<String, String> tokens=new HashMap<>();

    @Override
    public void start() throws Exception {
        HttpServerOptions options = new HttpServerOptions()
                .setPort(webSocketConfig.getPort());
        if (webSocketConfig.isSsl()) {
            options = options.setSsl(true)
                    .setKeyCertOptions(new PemKeyCertOptions()
                            .setKeyPath(webSocketConfig.getSslKey())
                            .setCertPath(webSocketConfig.getSslCert()));
        }
        httpServer = vertx.createHttpServer(options).webSocketHandler(wsClient -> {
            log.info("webSocket client connect sessionId:{},path={}", wsClient.textHandlerID(), wsClient.path());
            String deviceKey = wsClient.path().replace("/","");
            if(StringUtils.isBlank(deviceKey)||deviceKey.split("_").length!=2){
                log.warn("陌生连接，拒绝");
                wsClient.reject();
                return;
            }
            wsClient.writeTextMessage("connect succes! please auth!");
            Map<String,String> deviceKeyObj=new HashMap<>();
            deviceKeyObj.put("deviceKey",deviceKey);
            wsClient.textMessageHandler(message -> {
                HashMap<String,String> msg= JsonUtil.parse(message,HashMap.class);
                if(wsClients.containsKey(deviceKey)){
                    executor.onReceive(new HashMap<>(), "", message);
                }else if(msg!=null&&"auth".equals(msg.get("type"))){
                    Set<String> tokenKey=tokens.keySet();
                    for(String key:tokenKey){
                        if(StringUtils.isNotBlank(msg.get(key))&&tokens.get(key).equals(msg.get(key))){
                            //保存设备与连接关系
                            log.info("认证通过");
                            wsClients.put(deviceKey, wsClient);
                            wsClient.writeTextMessage("auth succes");
                            return;
                        }
                    }
                    log.warn("认证失败，拒绝");
                    wsClient.writeTextMessage("auth fail");
                }else{
                    log.warn("认证失败，拒绝");
                    wsClient.writeTextMessage("auth fail");
                }

            });
            wsClient.closeHandler(c -> {
                log.warn("client connection closed,deviceKey:{}", deviceKey);
                executor.onReceive(new HashMap<>(0), "disconnect", JsonUtil.toJsonString(deviceKeyObj), (r) -> {
                    //删除设备与连接关系
                    if(r!=null){
                        wsClients.remove(getDeviceKey(r));
                    }
                });
            });
            wsClient.exceptionHandler(ex -> {
                log.warn("webSocket client connection exception,deviceKey:{}", deviceKey);
            });
        }).listen(webSocketConfig.getPort(), server -> {
            if (server.succeeded()) {
                log.info("webSocket server is listening on port " + webSocketConfig.getPort());
                List<WebSocketServerConfig.AccessToken> tokenConfig= webSocketConfig.getAccessTokens();
                for (WebSocketServerConfig.AccessToken obj:tokenConfig) {
                    tokens.put(obj.getTokenName(),obj.getTokenStr());
                }
            } else {
                log.error("webSocket server on starting the server", server.cause());
            }
        });
    }

    @Override
    public void stop() throws Exception {
        for (String deviceKey : wsClients.keySet()) {
            Map<String,String> deviceKeyObj=new HashMap<>();
            deviceKeyObj.put("deviceKey",deviceKey);
            executor.onReceive(null, "disconnect", JsonUtil.toJsonString(deviceKeyObj));
        }
        tokens.clear();
        httpServer.close(voidAsyncResult -> log.info("close webocket server..."));
    }

    private String getDeviceKey(ReceiveResult result) {
        return getDeviceKey(result.getProductKey(), result.getDeviceName());
    }

    private String getDeviceKey(String productKey, String deviceName) {
        return String.format("%s_%s", productKey, deviceName);
    }

    @Override
    public DeviceMessage send(DeviceMessage message) {
        ServerWebSocket wsClient = wsClients.get(getDeviceKey(message.getProductKey(), message.getDeviceName()));
        Object obj = message.getContent();
        if (!(obj instanceof Map)) {
            throw new BizException("message content is not Map");
        }
        String msgStr = JsonUtil.toJsonString(obj);
        log.info("send msg payload:{}", msgStr);
        Future<Void> result = wsClient.writeTextMessage(msgStr);
        result.onFailure(e -> log.error("webSocket server send msg failed", e));
        return message;
    }
}
