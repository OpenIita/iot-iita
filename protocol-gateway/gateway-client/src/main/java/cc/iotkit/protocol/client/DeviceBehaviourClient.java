package cc.iotkit.protocol.client;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.protocol.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.*;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.impl.schema.JSONSchema;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DeviceBehaviourClient implements DeviceBehaviour {

    public final String gatewayServer;

    private GatewayConfig gatewayConfig;

    private final OkHttpClient httpClient;

    private Producer<DeviceMessage> deviceMessageProducer;

    private Producer<OtaMessage> otaMessageProducer;


    @SneakyThrows
    public DeviceBehaviourClient(String server) {
        //初始化协议网关http客户端
        this.gatewayServer = server.replaceAll("/$", "");
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        //获取协议网关配置
        getConfig();

        //初始化pulsar客户端
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(gatewayConfig.getMqServiceUrl())
                .build();

        deviceMessageProducer = client.newProducer(JSONSchema.of(DeviceMessage.class))
                .topic("persistent://public/default/device_raw")
                .create();

        otaMessageProducer = client.newProducer(JSONSchema.of(OtaMessage.class))
                .topic("persistent://public/default/device_ota")
                .create();
    }

    public Request buildRequest(String url, String method, String type, Map<String, Object> data) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);

        RequestBody requestBody;
        if ("json".equals(type)) {
            requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"),
                    JsonUtil.toJsonString(data));
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            data.forEach((key, val) -> builder.add(key, val.toString()));
            requestBody = builder.build();
        }
        requestBuilder.method(method.toUpperCase(), requestBody);
        return requestBuilder.build();
    }


    private <T> Result invoke(String path, T data) {
        return invoke(path, "form", data);
    }

    private <T> Result invokeJson(String path, T data) {
        return invoke(path, "json", data);
    }

    private <T> Result invoke(String path, String type, T data) {
        Request request = buildRequest(gatewayServer + path, "post", type,
                (data instanceof Map) ? (Map<String, Object>) data :
                        JsonUtil.parse(JsonUtil.toJsonString(data), Map.class));

        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            if (!response.isSuccessful() || response.body() == null) {
                return new Result(false, "Remote interface call failed:" + response.body().string());
            }
            String content = response.body().string();
            return JsonUtil.parse(content, Result.class);
        } catch (Throwable e) {
            return new Result(false, "Interface call failed:" + e.getMessage());
        }
    }

    private void getConfig() {
        if (this.gatewayConfig == null) {
            Result result = invoke("/getConfig", new HashMap<>());
            if (!result.isSuccess()) {
                throw new BizException("get gateway config failed");
            }
            this.gatewayConfig = JsonUtil.parse(result.getContent(), GatewayConfig.class);
        }
    }

    @Override
    public Result register(RegisterInfo info) {
        return invokeJson("/register", info);
    }

    @Override
    public Result online(String productKey, String deviceName) {
        Map<String, Object> data = new HashMap<>();
        data.put("productKey", productKey);
        data.put("deviceName", deviceName);
        return invoke("/online", data);
    }

    @Override
    public Result offline(String productKey, String deviceName) {
        Map<String, Object> data = new HashMap<>();
        data.put("productKey", productKey);
        data.put("deviceName", deviceName);
        return invoke("/offline", data);
    }

    @SneakyThrows
    @Override
    public void messageReport(DeviceMessage msg) {
        deviceMessageProducer.send(msg);
    }

    @Override
    public void otaProgressReport(OtaMessage msg) {
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GatewayConfig {

        private String mqServiceUrl;

    }
}
