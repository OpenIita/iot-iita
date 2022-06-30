package cc.iotkit.manager.controller;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.ThirdUserSessionRepository;
import cc.iotkit.model.ThirdUserSession;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Enumeration;

/**
 * 第三方接入认证
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class ThirdAuthController {

    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    @Autowired
    private ThirdUserSessionRepository thirdUserSessionRepository;

    @PostMapping("/token/{type}")
    public void getToken(
            @PathVariable("type") String type,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse)
            throws UnsupportedEncodingException {

        log.info("request:{}", JsonUtil.toJsonString(servletRequest.getParameterMap()));
        Enumeration<String> names = servletRequest.getParameterNames();
        StringBuilder sb = new StringBuilder();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            sb.append(name).append("=")
                    .append(URLEncoder.encode(servletRequest.getParameter(name), "UTF-8"))
                    .append("&");
        }

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, sb.toString());

        Request.Builder builder = new Request.Builder();
        Request request = builder.url("https://auth.iotkit.cc/realms/iotkit/protocol/openid-connect/token")
                .method("POST", body)
                .build();
        log.info("send request body:{}", sb.toString());

        Response response;
        try {
            response = client.newCall(request).execute();
            servletResponse.setStatus(response.code());
            Headers headers = response.headers();
            for (String name : headers.names()) {
                log.info("response header,name:{},value:{}", name, headers.get(name));
                servletResponse.setHeader(name, headers.get(name));
            }
            String bodyStr = response.body().string();
            log.info("response body:{}", bodyStr);
            TokenInfo tokenInfo = JsonUtil.parse(bodyStr, TokenInfo.class);
            String accessToken = tokenInfo.getAccess_token();
            String[] tokenParts = accessToken.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payloadStr = new String(decoder.decode(tokenParts[1]));
            TokenPayload payload = JsonUtil.parse(payloadStr, TokenPayload.class);
            log.info("token payload:{}", payloadStr);

            //保存用户授权token
            String uid = payload.getSub();
            thirdUserSessionRepository.save(ThirdUserSession.builder()
                    .uid(uid)
                    .token(accessToken)
                    .type(type)
                    .authAt(System.currentTimeMillis())
                    .build());

            servletResponse.setContentType("application/json");
            servletResponse.getWriter().write(bodyStr);
        } catch (IOException e) {
            log.error("request error", e);
        }
    }

    @Data
    public static class TokenInfo {
        private String access_token;
    }

    @Data
    public static class TokenPayload {
        private String sub;
    }
}
