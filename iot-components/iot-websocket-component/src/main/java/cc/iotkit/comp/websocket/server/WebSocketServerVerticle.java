package cc.iotkit.comp.websocket.server;


import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
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

    private WebSocketServerConfig webSocketConfig;

    private final Map<String, ServerWebSocket> wsClients = new ConcurrentHashMap<>();

    public WebSocketServerVerticle(String config) {
        this.webSocketConfig = JsonUtil.parse(config, WebSocketServerConfig.class);
    }

    private Map<String, String> tokens=new HashMap<>();

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
                    if("ping".equals(msg.get("type"))){
                        msg.put("type","pong");
                        wsClient.writeTextMessage(JsonUtil.toJsonString(msg));
                        return;
                    }
                    if("register".equals(msg.get("type"))){
                        executor.onReceive(new HashMap<>(), "", message,(r) -> {
                            if (r == null) {
                                //注册失败
                                Map<String,String> ret=new HashMap<>();
                                ret.put("id",msg.get("id"));
                                ret.put("type",msg.get("type"));
                                ret.put("result","fail");
                                wsClient.writeTextMessage(JsonUtil.toJsonString(ret));
                                return;
                            }else{
                                msg.put("type","online");
                                executor.onReceive(new HashMap<>(), "", JsonUtil.toJsonString(msg));
                            }
                        });
                    }
                }else if(msg!=null&&"auth".equals(msg.get("type"))){
                    Set<String> tokenKey=tokens.keySet();
                    for(String key:tokenKey){
                        if(StringUtils.isNotBlank(msg.get(key))&&tokens.get(key).equals(msg.get(key))){
                            //保存设备与连接关系
                            wsClients.put(deviceKey, wsClient);
                            log.info("认证通过：{}||||||{}",wsClients.size(),deviceKey);
                            wsClient.writeTextMessage("auth succes");
                            return;
                        }
                    }
                    log.warn("认证失败，拒绝");
                    wsClient.writeTextMessage("auth fail");
                    return;
                }else{
                    log.warn("认证失败，拒绝");
                    wsClient.writeTextMessage("auth fail");
                    return;
                }

            });
            wsClient.closeHandler(c -> {
                log.warn("client connection closed,deviceKey:{}", deviceKey);
                if(wsClients.containsKey(deviceKey)){
                    wsClients.remove(deviceKey);
                    deviceKeyObj.put("type","offline");
                    executor.onReceive(new HashMap<>(), "", JsonUtil.toJsonString(deviceKeyObj), (r) -> {
                    });
                }
            });
            wsClient.exceptionHandler(ex -> {
                log.warn("webSocket client connection exception,deviceKey:{}", deviceKey);
                if(wsClients.containsKey(deviceKey)){
                    wsClients.remove(deviceKey);
                    deviceKeyObj.put("type","offline");
                    executor.onReceive(new HashMap<>(), "", JsonUtil.toJsonString(deviceKeyObj), (r) -> {
                    });
                }
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
            deviceKeyObj.put("type","offline");
            executor.onReceive(null, "", JsonUtil.toJsonString(deviceKeyObj));
        }
        tokens.clear();
        httpServer.close(voidAsyncResult -> log.info("close webocket server..."));
    }

    private String getDeviceKey(String productKey, String deviceName) {
        return String.format("%s_%s", productKey, deviceName);
    }

    @Override
    public DeviceMessage send(DeviceMessage message) {
        ServerWebSocket wsClient = wsClients.get(message.getDeviceName());
        log.info("DeviceMessage：{}-{}-{}", wsClients.size(),message.getDeviceName(),message.getProductKey());
        for (String key:wsClients.keySet()) {
            log.info("key：", key);
        }
        Object obj = message.getContent();
        if (!(obj instanceof Map)) {
            throw new BizException(ErrCode.DATA_FORMAT_ERROR);
        }
        String msgStr = JsonUtil.toJsonString(obj);
        log.info("send msg payload:{}", msgStr);
        Future<Void> result = wsClient.writeTextMessage(msgStr);
        result.onFailure(e -> log.error("webSocket server send msg failed", e));
        return message;
    }
}
