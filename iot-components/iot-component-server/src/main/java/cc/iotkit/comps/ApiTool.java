/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comps;

import cc.iotkit.common.constant.Constants;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 平台API调用工具类
 */
@Slf4j
public class ApiTool {

    private final Vertx vertx;
    private final WebClient client;

    private String host;
    private int port;
    private int timeout;

    public ApiTool() {
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

    public void config(String host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    private String getDevicePath(String path) {
        return Paths.get(Constants.API_DEVICE.BASE, path).toString();
    }

    private String getSpacePath(String path) {
        return Paths.get(Constants.API_SPACE.BASE, path).toString();
    }

    /**
     * 获取用户空间中设备列表
     */
    public ApiResponse getSpaceDevices(String token) {
        HttpRequest<Buffer> request = client
                .get(port, host, getSpacePath(Constants.API_SPACE.SPACE_DEVICES
                        .replace("{spaceId}", "all")));
        return send(token, HttpMethod.GET, request, new HashMap<>());
    }

    /**
     * 获取空间设备详情
     */
    public ApiResponse getSpaceDeviceDetail(String token, String deviceId) {
        HttpRequest<Buffer> request = client
                .get(port, host, getSpacePath(Constants.API_SPACE.GET_DEVICE
                        .replace("{deviceId}", deviceId)));
        return send(token, HttpMethod.GET, request, new HashMap<>());
    }

    /**
     * 获取用户的设备列表
     */
    public ApiResponse getDevices(String token) {
        HttpRequest<Buffer> request = client
                .post(port, host, getDevicePath(Constants.API_DEVICE.LIST
                        .replace("{size}", "1000")
                        .replace("{page}", "1")));
        return send(token, HttpMethod.POST, request, new HashMap<>());
    }

    /**
     * 设置第三方平台的openUid
     */
    public ApiResponse setOpenUid(String token, String deviceId, String platform, String openUid) {
        HttpRequest<Buffer> request = client
                .post(port, host, getSpacePath(Constants.API_SPACE.SET_OPEN_UID));
        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", deviceId);
        map.put("platform", platform);
        map.put("openUid", openUid);
        return send(token, HttpMethod.POST, request, map, false);
    }

    /**
     * 获取设备详情
     */
    public ApiResponse getDeviceDetail(String token, String deviceId) {
        HttpRequest<Buffer> request = client
                .get(port, host, getDevicePath(Constants.API_DEVICE.DETAIL
                        .replace("{deviceId}", deviceId)));
        return send(token, HttpMethod.GET, request, new HashMap<>());
    }

    /**
     * 设置属性
     */
    public ApiResponse setProperties(String token, String deviceId, Map<String, Object> properties) {
        HttpRequest<Buffer> request = client
                .post(port, host, getDevicePath(Constants.API_DEVICE.SET_PROPERTIES
                        .replace("{deviceId}", deviceId)));
        return send(token, HttpMethod.POST, request, properties);
    }

    /**
     * 调用服务
     */
    public ApiResponse invokeService(String token, String deviceId, String service, Map<String, Object> params) {
        HttpRequest<Buffer> request = client
                .post(port, host, getDevicePath(Constants.API_DEVICE.INVOKE_SERVICE
                        .replace("{deviceId}", deviceId)
                        .replace("{service}", service)));
        return send(token, HttpMethod.POST, request, params);
    }

    private ApiResponse send(String token, HttpMethod method,
                             HttpRequest<Buffer> request,
                             Map<String, Object> params) {
        return send(token, method, request, params, true);
    }

    private ApiResponse send(String token, HttpMethod method,
                             HttpRequest<Buffer> request,
                             Map<String, Object> params,
                             boolean isJson
    ) {
        request = request
                .timeout(timeout)
                .putHeader("wrap-response", "json")
                .putHeader("token", token);

        AtomicReference<ApiResponse> apiResponse = new AtomicReference<>(
                new ApiResponse(500, "", null, System.currentTimeMillis()));
        try {
            //转为同步模式便于提供给js调用
            CountDownLatch wait = new CountDownLatch(1);

            if (method == HttpMethod.POST && isJson) {
                request.sendJson(params)
                        .onSuccess((response) -> onSendSuccess(apiResponse, wait, response))
                        .onFailure((err) -> onSendFail(wait, err));
            } else if (method == HttpMethod.POST) {
                //添加表单参数
                MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
                params.forEach((k, v) -> multiMap.add(k, v.toString()));
                request.sendForm(multiMap)
                        .onSuccess((response) -> onSendSuccess(apiResponse, wait, response))
                        .onFailure((err) -> onSendFail(wait, err));
            } else {
                request.send()
                        .onSuccess((response) -> onSendSuccess(apiResponse, wait, response))
                        .onFailure((err) -> onSendFail(wait, err));
            }

            if (wait.await(timeout, TimeUnit.MILLISECONDS)) {
                return apiResponse.get();
            } else {
                apiResponse.get().setStatus(500);
                apiResponse.get().setMessage("request timeout");
            }
        } catch (Throwable e) {
            apiResponse.get().setStatus(500);
            apiResponse.get().setMessage(e.getMessage());
            log.error("send error", e);
        }
        return apiResponse.get();
    }

    private void onSendFail(CountDownLatch wait, Throwable err) {
        log.error("send failed", err);
        wait.countDown();
    }

    private void onSendSuccess(AtomicReference<ApiResponse> apiResponse, CountDownLatch wait, HttpResponse<Buffer> response) {
        log.info("send succeed,response:{}", response.bodyAsString());
        apiResponse.set(response.bodyAsJson(ApiResponse.class));
        wait.countDown();
    }

    public void log(String msg) {
        log.info(msg);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse {
        private int status;
        private String message;
        private Object data;
        private long timestamp;
    }
}
