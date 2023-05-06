/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.config;

import cn.dev33.satoken.util.SaResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String wrapResponse = request.getHeader("wrap-response");
        return "json".equals(wrapResponse);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof GlobalExceptionHandler.RequestResult) {
            GlobalExceptionHandler.RequestResult requestResult = (GlobalExceptionHandler.RequestResult) body;
            return new ApiResponse(requestResult.getCode(), requestResult.getMessage(),
                    "", System.currentTimeMillis());
        } else if (body instanceof SaResult) {
            SaResult result = (SaResult) body;
            return new ApiResponse(result.getCode(), result.getMsg(), result.getData(), System.currentTimeMillis());
        } else if (body instanceof Map) {
            Map map = (Map) body;
            //spring mvc内部异常
            if (map.containsKey("timestamp") && map.containsKey("status") && map.containsKey("error")) {
                return new ApiResponse((Integer) map.get("status"), (String) map.get("error"),
                        "", System.currentTimeMillis());
            }
        }

        return new ApiResponse(200, "", body, System.currentTimeMillis());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse {
        private int code;
        private String message;
        private Object data;
        private long timestamp;
    }
}
