package cc.iotkit.comp.biz;

import cc.iotkit.common.Constants;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 平台API调用工具类
 */
public class ApiTool {

    private static final Vertx vertx;
    private static final WebClient client;

    static {
        if (Vertx.currentContext() == null) {
            vertx = Vertx.vertx();
        } else {
            vertx = Vertx.currentContext().owner();
        }

        WebClientOptions options = new WebClientOptions()
                .setUserAgent("component-api-tool");
        options.setKeepAlive(false);
        client = WebClient.create(vertx, options);
    }

    private static String host;
    private static int port;
    private static int timeout;

    public static void config(String host, int port, int timeout) {
        ApiTool.host = host;
        ApiTool.port = port;
        ApiTool.timeout = timeout;
    }

    /**
     * 获取用户的设备列表
     */
    public static ApiResponse getDevices(String token) {
        HttpRequest<Buffer> request = client
                .post(port, host, Paths.get(Constants.API.DEVICE_BASE, Constants.API.DEVICE_LIST
                        .replace("size", "1000")
                        .replace("page", "1")).toString())
                .timeout(timeout)
                .putHeader("authorization", "Bearer " + token);
        return sendJson(request, new HashMap<>());
    }

    /**
     * 获取设备详情
     */
    public static ApiResponse getDeviceDetail(String token, String deviceId) {
        HttpRequest<Buffer> request = client
                .post(port, host, Paths.get(Constants.API.DEVICE_BASE, Constants.API.DEVICE_DETAIL
                        .replace("deviceId", deviceId)).toString())
                .timeout(timeout)
                .putHeader("authorization", "Bearer " + token);
        return sendJson(request, new HashMap<>());
    }

    /**
     * 设置属性
     */
    public static ApiResponse setProperties(String token, String deviceId, Map<String, Object> properties) {
        HttpRequest<Buffer> request = client
                .post(port, host, Paths.get(Constants.API.DEVICE_BASE, Constants.API.DEVICE_SET_PROPERTIES
                        .replace("deviceId", deviceId)).toString())
                .timeout(timeout)
                .putHeader("authorization", "Bearer " + token);
        return sendJson(request, properties);
    }

    /**
     * 调用服务
     */
    public static ApiResponse invokeService(String token, String deviceId, String service, Map<String, Object> params) {
        HttpRequest<Buffer> request = client
                .post(port, host, Paths.get(Constants.API.DEVICE_BASE, Constants.API.DEVICE_INVOKE_SERVICE
                        .replace("deviceId", deviceId)
                        .replace("service", service)).toString())
                .timeout(timeout)
                .putHeader("authorization", "Bearer " + token);
        return sendJson(request, params);
    }

    private static ApiResponse sendJson(HttpRequest<Buffer> request, Map<String, Object> params) {
        AtomicReference<ApiResponse> apiResponse = new AtomicReference<>(new ApiResponse(500, "", null));
        try {
            //转为同步模式便于提供给js调用
            CountDownLatch wait = new CountDownLatch(1);
            request.sendJson(params)
                    .onSuccess((response) -> {
                        System.out.println(response.bodyAsString());
                        apiResponse.set(response.bodyAsJson(ApiResponse.class));
                        wait.countDown();
                    })
                    .onFailure((err) -> {
                        err.printStackTrace();
                        wait.countDown();
                    });
            if (wait.await(timeout, TimeUnit.MILLISECONDS)) {
                return apiResponse.get();
            } else {
                apiResponse.get().setCode(500);
                apiResponse.get().setMessage("request timeout");
            }
        } catch (Throwable e) {
            apiResponse.get().setCode(500);
            apiResponse.get().setMessage(e.getMessage());
        }
        return apiResponse.get();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse {
        private int code;
        private String message;
        private Object data;
    }
}
