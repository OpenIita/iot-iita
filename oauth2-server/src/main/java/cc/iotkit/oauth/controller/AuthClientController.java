package cc.iotkit.oauth.controller;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.dao.OauthClientCache;
import cc.iotkit.dao.UserInfoCache;
import cc.iotkit.model.OauthClient;
import cc.iotkit.model.UserInfo;
import cc.iotkit.utils.SoMap;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.ejlchina.okhttps.OkHttps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

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

    // 进入首页
    @RequestMapping("/")
    public Object index(HttpServletRequest request) {
        request.setAttribute("uid", StpUtil.getLoginIdDefaultNull());
        return new ModelAndView("index.html");
    }

    // 根据Code码进行登录，获取 Access-Token 和 openid
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
        // code不等于200  代表请求失败
        if (so.getInt("code") != 200) {
            return SaResult.error(so.getString("msg"));
        }

        // 根据openid获取其对应的userId
        SoMap data = so.getMap("data");
        String uid = getUserIdByOpenid(data.getString("openid"));
        String access_token = data.getString("access_token");
        UserInfo userInfo = userInfoCache.getUserInfo(uid);
        data.put("name", userInfo.getNickName());
        data.put("uid", uid);

        // 返回相关参数
        StpUtil.login(uid, SaLoginConfig.setToken(access_token));
        return SaResult.data(data);
    }

    // 注销登录
    @RequestMapping("/logout")
    public RedirectView logout(String accessToken, String redirect_uri) {
        //先注销client中cookie的token
        StpUtil.logout();
        //再注销web页面使用的token
        StpUtil.logoutByTokenValue(accessToken);

        return new RedirectView(redirect_uri);
    }

    // 根据 Access-Token 置换相关的资源: 获取账号昵称、头像、性别等信息
    @RequestMapping("/getUserinfo")
    public SaResult getUserinfo(String accessToken) {
        // 调用Server端接口，查询开放的资源
        String str = OkHttps.sync(serverUrl + "/oauth2/userinfo")
                .addBodyPara("access_token", accessToken)
                .post()
                .getBody()
                .toString();
        SoMap so = SoMap.getSoMap().setJsonString(str);
        // code不等于200  代表请求失败
        if (so.getInt("code") != 200) {
            return SaResult.error(so.getString("msg"));
        }

        // 返回相关参数 (data=获取到的资源 )
        SoMap data = so.getMap("data");
        return SaResult.data(data);
    }

    @GetMapping("/checkLogin")
    public SaResult checkLogin() {
        try {
            StpUtil.checkLogin();
        } catch (Throwable e) {
            return SaResult.error("no login");
        }
        return SaResult.ok();
    }

    @SneakyThrows
    private String getUserIdByOpenid(String openid) {
        String clientIdLoginId = CodecUtil.aesDecrypt(openid, Constants.ACCOUNT_SECRET);
        return clientIdLoginId.split(":")[1];
    }

}
