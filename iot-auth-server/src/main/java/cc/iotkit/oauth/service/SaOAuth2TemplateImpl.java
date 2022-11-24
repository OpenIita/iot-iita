/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.oauth.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.data.IOauthClientData;
import cc.iotkit.model.OauthClient;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.dev33.satoken.stp.StpUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SaOAuth2TemplateImpl extends SaOAuth2Template {

    @Autowired
    @Qualifier("oauthClientDataCache")
    private IOauthClientData oauthClientData;

    // 根据 id 获取 Client 信息
    @Override
    public SaClientModel getClientModel(String clientId) {
        OauthClient client = oauthClientData.findByClientId(clientId);
        if (client == null) {
            return null;
        }

        return new SaClientModel()
                .setClientId(client.getClientId())
                .setClientSecret(client.getClientSecret())
                .setAllowUrl(client.getAllowUrl())
                .setContractScope("userinfo")
                .setIsAutoMode(true);
    }

    // 根据ClientId 和 LoginId 获取openid
    @SneakyThrows
    @Override
    public String getOpenid(String clientId, Object loginId) {
        // 此为模拟数据，真实环境需要从数据库查询
        return CodecUtil.aesEncrypt(clientId + ":" + loginId, Constants.ACCOUNT_SECRET);
    }

    @Override
    public String randomAccessToken(String clientId, Object loginId, String scope) {
        return StpUtil.createLoginSession(loginId);
    }


}
