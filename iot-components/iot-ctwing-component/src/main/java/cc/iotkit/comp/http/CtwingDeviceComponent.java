/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.http;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.AbstractDeviceComponent;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.converter.DeviceMessage;
import com.ctg.ag.sdk.biz.AepDeviceCommandClient;
import com.ctg.ag.sdk.biz.aep_device_command.CreateCommandRequest;
import com.ctg.ag.sdk.biz.aep_device_command.CreateCommandResponse;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * 电信天翼接入组件
 */
@Slf4j
public class CtwingDeviceComponent extends AbstractDeviceComponent {

    private final Vertx vertx = Vertx.vertx();

    private CtwingConfig ctwingConfig;

    private HttpServer backendServer;

    private AepDeviceCommandClient commandClient;

    @Override
    public void create(CompConfig config) {
        super.create(config);
        this.ctwingConfig = JsonUtil.parse(config.getOther(), CtwingConfig.class);
        commandClient = AepDeviceCommandClient.newClient()
                .appKey(ctwingConfig.getAppKey())
                .appSecret(ctwingConfig.getAppSecret())
                .build();
    }

    @Override
    public void start() {
        backendServer = vertx.createHttpServer();
        Router backendRouter = Router.router(vertx);

        backendRouter.route().handler(BodyHandler.create())
                .handler(rc -> {
                    try {
                        Map<String, Object> httpHeader = ProtocolUtil.getData(rc.request().headers());
                        log.info("request header:{}", JsonUtil.toJsonString(httpHeader));
                        Map<String, List<Object>> httpParams = ProtocolUtil.getListData(rc.request().params());
                        log.info("request params:{}", JsonUtil.toJsonString(httpParams));

                        HttpServerRequest httpRequest = rc.request();
                        String contentType = httpRequest.headers().get("Content-Type");
                        String requestBody = "";
                        int responseCode = 500;
                        if ("application/json".equals(contentType)) {
                            requestBody = rc.getBody().toString();
                            EncodedMessage msg = JsonUtil.parse(requestBody, EncodedMessage.class);
                            String content = CodecUtil.aesDecrypt(ctwingConfig.getEncryptToken(), msg.getEnc_msg());
                            log.info("decrypt msg:{}", content);
                            getHandler().onReceive(httpHeader, "", content);
                            responseCode = 200;
                        }
                        log.info("request body:{}", requestBody);

                        rc.response().setStatusCode(responseCode)
                                .end();
                    } catch (Throwable e) {
                        log.error("handle request error", e);
                        rc.response().setStatusCode(500).end();
                    }
                });

        backendServer.requestHandler(backendRouter)
                .listen(ctwingConfig.getPort(), (http) -> {
                    if (http.succeeded()) {
                        log.info("http server create succeed,port:{}", ctwingConfig.getPort());
                    } else {
                        log.error("http server create failed", http.cause());
                    }
                });
    }

    @Override
    public DeviceMessage send(DeviceMessage message) {
        Object obj = message.getContent();
        if (!(obj instanceof Map)) {
            throw new BizException("message content is not Map");
        }
        SendContent msg = new SendContent();
        try {
            BeanUtils.populate(msg, (Map<String, ? extends Object>) obj);
        } catch (Throwable e) {
            throw new BizException("message content is incorrect");
        }

        CreateCommandRequest request = new CreateCommandRequest();
        request.setParamMasterKey(msg.getMasterKey());
        request.setBody(("{\n" +
                "    \"content\":{\n" +
                "        \"dataType\":2,\n" +
                "        \"payload\":\"" + msg.getPayload() + "\"\n" +
                "    },\n" +
                "    \"deviceId\":\"" + message.getDeviceName() + "\",\n" +
                "    \"operator\":\"none\",\n" +
                "    \"productId\":" + msg.getProductId() + ",\n" +
                "    \"ttl\":0,\n" +
                "    \"level\":1\n" +
                "}").getBytes());
        CreateCommandResponse response;
        try {
            response = commandClient.CreateCommand(request);
        } catch (Exception e) {
            throw new RuntimeException("send cmd to ctwing error", e);
        }

        String body = new String(response.getBody());
        log.info("send ctwing cmd result:{}", body);
        if (response.getStatusCode() != 200) {
            throw new RuntimeException("send cmd to ctwing error:" + body);
        }

        CtwingCmdRsp cmdRsp = JsonUtil.parse(body, CtwingCmdRsp.class);
        if (cmdRsp.code != 0) {
            throw new RuntimeException("send cmd to ctwing failed:" + body);
        }

        return message;
    }

    @Override
    public void stop() {
        backendServer.close();
    }

    @Override
    public void destroy() {
    }

    /**
     * 将数据编码成68H16H协议数据包，给js调用
     */
    public String encode68H16H(String devId, String cardNo, Object[] values) {
        return ProtocolUtil.encode68H16H(devId, cardNo, values);
    }

    /**
     * 将68H16H协议的base64字符串消息解码为map数据，给js调用
     */
    public Map<String, Object> decode68H16H(String base64Str) {
        return ProtocolUtil.decode68H16H(base64Str);
    }

    @Data
    public static class EncodedMessage {
        private String msg_signature;
        private String enc_msg;
    }

    @Data
    public static class SendContent {
        private String masterKey;
        private String productId;
        private String payload;
    }


    @Data
    public static class CtwingCmdRsp {
        private int code;
        protected String msg;
        protected CmdResult result;
    }

    @Data
    public static class CmdResult {
        private String commandId;
        private String command;
        private String commandStatus;
        private int productId;
        private String deviceId;
        private String imei;
        private String createBy;
        private String createTime;
        private int ttl;
    }

}
