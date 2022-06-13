package cc.iotkit.oauth.controller;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.dao.OauthClientCache;
import cc.iotkit.dao.UserInfoCache;
import cc.iotkit.model.OauthClient;
import cc.iotkit.model.UserInfo;
import cc.iotkit.oauth.vo.UserInfoVo;
import cc.iotkit.utils.SoMap;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.ejlchina.okhttps.OkHttps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/oauth2")
public class AuthClientController {

    @Value("${oauth2.auth-server-url}")
    private String serverUrl;

    @Autowired
    private OauthClientCache oauthClientCache;
    @Autowired
    private UserInfoCache userInfoCache;

    /**
     * 根据Code码进行登录，获取 Access-Token 和 用户信息
     */
    @RequestMapping("/codeLogin")
    public SaResult codeLogin(String code, String clientId) {
        OauthClient oauthClient = oauthClientCache.getClient(clientId);
        if (oauthClient == null) {
            return SaResult.error("clientId does not exist");
        }
        String clientSecret = oauthClient.getClientSecret();

        // 调用Server端接口，获取 Access-Token 以及其他信息
        String str = OkHttps.sync(serverUrl + "/oauth2/token")
                .addBodyPara("grant_type", "authorization_code")
                .addBodyPara("code", code)
                .addBodyPara("client_id", clientId)
                .addBodyPara("client_secret", clientSecret)
                .post()
                .getBody()
                .toString();
        SoMap so = SoMap.getSoMap().setJsonString(str);
        log.info("get token by code result:{}", so);
        // 存在code,不是token结构
        if (so.getInt("code") != 0) {
            return SaResult.error(so.getString("msg"));
        }

        // 根据openid获取其对应的userId
        String uid = getUserIdByOpenid(so.getString("openid"));
        String access_token = so.getString("access_token");
        UserInfoVo userVo = getUserInfo(uid);
        BeanMap beanMap = BeanMap.create(userVo);
        Map<String, Object> data = new HashMap<>();
        beanMap.forEach((key, value) -> {
            data.put(key.toString(), value);
        });
        data.put("access_token", access_token);

        // 返回相关参数
        StpUtil.login(uid, SaLoginConfig.setToken(access_token));
        return SaResult.data(data);
    }

    /**
     * 注销登录
     */
    @RequestMapping("/logout")
    public RedirectView logout(String accessToken, String redirect_uri) {
        //先注销client中cookie的token
        StpUtil.logout();
        //再注销web页面使用的token
        StpUtil.logoutByTokenValue(accessToken);

        return new RedirectView(redirect_uri);
    }

    /**
     * 登录验证
     */
    @GetMapping("/checkLogin")
    public SaResult checkLogin(HttpServletResponse response) {
        try {
            String uid = StpUtil.getLoginId().toString();
            UserInfoVo userVo = getUserInfo(uid);
            return SaResult.ok().setData(userVo);
        } catch (Throwable e) {
            response.addCookie(new Cookie("token", ""));
            return SaResult.error("no login");
        }
    }

    @SneakyThrows
    private String getUserIdByOpenid(String openid) {
        String clientIdLoginId = CodecUtil.aesDecrypt(openid, Constants.ACCOUNT_SECRET);
        return clientIdLoginId.split(":")[1];
    }

    private UserInfoVo getUserInfo(String uid) {
        UserInfo userInfo = userInfoCache.getUserInfo(uid);
        UserInfoVo userVo = new UserInfoVo();
        ReflectUtil.copyNoNulls(userInfo, userVo);
        return userVo;
    }

}
