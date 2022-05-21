package cc.iotkit.oauth.controller;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.model.UserInfo;
import cc.iotkit.oauth.service.TokenRequestHandler;
import cc.iotkit.utils.AuthUtil;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
public class AuthServerController {

    @Autowired
    private UserInfoRepository userInfoRepository;

    // 处理所有OAuth相关请求
    @RequestMapping("/oauth2/*")
    public Object request(HttpServletRequest request) {
        Object result = TokenRequestHandler.serverRequest();
        log.info("oauth path:{},result:{}", request.getRequestURI(), JsonUtil.toJsonString(result));
        return result;
    }

    // Sa-OAuth2 定制化配置
    @Autowired
    public void setSaOAuth2Config(SaOAuth2Config cfg) {
        cfg.
                // 未登录的视图
                        setNotLoginView(() -> new ModelAndView("login.html")).
                // 登录处理函数
                        setDoLoginHandle((name, pwd) -> {
                    try {
                        UserInfo userInfo = userInfoRepository.findByUid(name);
                        if (userInfo != null) {
                            String secret = userInfo.getSecret();
                            if (AuthUtil.checkPwd(pwd, secret)) {
                                StpUtil.login(userInfo.getId(), "PC");
                                return SaResult.ok();
                            }
                        }
                    } catch (Throwable e) {
                        return SaResult.error("账号名或密码错误");
                    }
                    return SaResult.error("账号名或密码错误");
                }).
                // 授权确认视图
                        setConfirmView((clientId, scope) -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("clientId", clientId);
                    map.put("scope", scope);
                    return new ModelAndView("confirm.html", map);
                })
        ;
    }

    // 全局异常拦截
    @ExceptionHandler
    public SaResult handlerException(Exception e) {
        e.printStackTrace();
        return SaResult.error(e.getMessage());
    }

    // ---------- 开放相关资源接口： Client端根据 Access-Token ，置换相关资源 ------------

    // 获取Userinfo信息：昵称、头像、性别等等
    @RequestMapping("/oauth2/userinfo")
    public SaResult userinfo() {
        // 获取 Access-Token 对应的账号id
        String accessToken = SaHolder.getRequest().getParamNotNull("access_token");
        Object loginId = SaOAuth2Util.getLoginIdByAccessToken(accessToken);
        System.out.println("-------- 此Access-Token对应的账号id: " + loginId);

        // 校验 Access-Token 是否具有权限: userinfo
        SaOAuth2Util.checkScope(accessToken, "userinfo");

        // 模拟账号信息 （真实环境需要查询数据库获取信息）
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("nickname", "shengzhang_");
        map.put("avatar", "http://xxx.com/1.jpg");
        map.put("age", "18");
        map.put("sex", "男");
        map.put("address", "山东省 青岛市 城阳区");
        return SaResult.data(map);
    }


}
