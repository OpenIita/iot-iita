/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.oauth.controller;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.data.IUserInfoData;
import cc.iotkit.model.UserInfo;
import cc.iotkit.oauth.service.TokenRequestHandler;
import cc.iotkit.utils.AuthUtil;
import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
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
import java.util.Map;

@Slf4j
@RestController
public class AuthServerController {

    @Autowired
    private IUserInfoData userInfoData;

    /**
     * 处理所有OAuth相关请求
     */
    @RequestMapping("/oauth2/*")
    public Object request(HttpServletRequest request) {
        Object result = TokenRequestHandler.serverRequest();
        log.info("oauth path:{},result:{}", request.getRequestURI(), JsonUtil.toJsonString(result));
        return result;
    }

    /**
     * Sa-OAuth2 自定义配置
     */
    @Autowired
    public void setSaOAuth2Config(SaOAuth2Config cfg) {
        cfg.
                // 未登录的视图
                        setNotLoginView(() -> new ModelAndView("login.html")).
                // 登录处理函数
                        setDoLoginHandle((name, pwd) -> {
                    try {
                        UserInfo userInfo = userInfoData.findByUid(name);
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

        //开启密码授权、刷新token和client授权模式
        cfg.setIsPassword(true);
        cfg.setIsNewRefresh(true);
        cfg.setIsClient(true);
    }

    // 全局异常拦截
    @ExceptionHandler
    public SaResult handlerException(Exception e) {
        e.printStackTrace();
        return SaResult.error(e.getMessage());
    }

    @RequestMapping("/oauth2/userinfo")
    public SaResult userinfo() {
        return SaResult.ok();
    }

}
