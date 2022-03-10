package cc.iotkit.protocol.client;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.protocol.*;
import okhttp3.*;
import org.apache.pulsar.client.api.PulsarClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DeviceBehaviourClient implements DeviceBehaviour {

    private final String server;

    private final OkHttpClient httpClient;

    private PulsarClient client;

    public DeviceBehaviourClient(String server) {
        this.server = server.replaceAll("/$", "");
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
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
        Request request = buildRequest(server + path, "post", "form",
                (data instanceof Map) ? (Map<String, Object>) data :
                        JsonUtil.parse(JsonUtil.toJsonString(data), Map.class));

        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            if (!response.isSuccessful() || response.body() == null) {
                return new Result(false, "接口调用失败");
            }
            String content = response.body().string();
            return JsonUtil.parse(content, Result.class);
        } catch (Throwable e) {
            return new Result(false, "接口调用失败:" + e.getMessage());
        }
    }


    @Override
    public Result register(RegisterInfo info) {
        return invoke("/register", info);
    }

    @Override
    public Result deregister(DeregisterInfo info) {
        return invoke("/deregister", info);
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

    @Override
    public void messageReport(DeviceMessage msg) {

    }

    @Override
    public void otaProgressReport(OtaMessage msg) {
    }
}
