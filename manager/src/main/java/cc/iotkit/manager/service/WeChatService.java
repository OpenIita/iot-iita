/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.manager.utils.WeChatUtil;
import cc.iotkit.model.UserInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WeChatService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    public String login(String encryptedData, String iv, String loginCode) {
        WxSession wxSession = authCode2Session(Constants.WECHAT_APP_ID, Constants.WECHAT_APP_SECRET, loginCode);
        if (wxSession == null) {
            throw new RuntimeException("调用微信端授权认证接口错误");
        }
        if (StringUtils.isEmpty(wxSession.getOpenid())) {
            throw new RuntimeException("微信授权认证失败");
        }
        if (wxSession.getErrcode() != 0) {
            throw new RuntimeException("微信授权认证失败:" + wxSession.getErrmsg());
        }

        UserInfo userInfo = userInfoRepository.findById(wxSession.getOpenid()).orElse(null);
        //判断用户表中是否存在该用户，不存在则进行解密得到用户信息，并进行新增用户
        String strUserInfo = WeChatUtil.decryptData(encryptedData, wxSession.getSession_key(), iv);
        if (StringUtils.isEmpty(strUserInfo)) {
            throw new RuntimeException("解密用户信息错误");
        }
        UserInfo decryptUser = JsonUtil.parse(strUserInfo, UserInfo.class);
        if (userInfo == null) {
        } else {
            decryptUser.setId(userInfo.getId());
        }
//        decryptUser.setId(decryptUser.getOpenId());
        userInfoRepository.save(decryptUser);

        try {
            return CodecUtil.aesEncrypt(System.currentTimeMillis() + "_" + wxSession.getOpenid(), Constants.ACCOUNT_SECRET);
        } catch (Throwable e) {
            throw new RuntimeException("微信授权认证失败");
        }
    }

    public WxSession authCode2Session(String appId, String secret, String jsCode) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + jsCode + "&grant_type=authorization_code";
        String str = WeChatUtil.httpRequest(url, "GET", null);
        log.info("api/wx-mini/getSessionKey:" + str);
        if (StringUtils.isBlank(str)) {
            return null;
        } else {
            return JsonUtil.parse(str, WxSession.class);
        }
    }

    @Data
    public static class WxSession {

        private String openid;

        private String session_key;

        private String unionid;

        private int errcode;

        private String errmsg;
    }

}
