package cc.iotkit.oauth.service;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.oauth2.SaOAuth2Manager;
import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.dev33.satoken.oauth2.exception.SaOAuth2Exception;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Handle;
import cn.dev33.satoken.oauth2.model.SaClientModel;

public class TokenRequestHandler {

    public static Object serverRequest() {
        SaRequest req = SaHolder.getRequest();
        SaResponse res = SaHolder.getResponse();
        SaOAuth2Config cfg = SaOAuth2Manager.getConfig();
        SaClientModel cm;
        if (req.isPath(SaOAuth2Consts.Api.authorize) && req.isParam(SaOAuth2Consts.Param.response_type, SaOAuth2Consts.ResponseType.code)) {
            cm = SaOAuth2Handle.currClientModel();
            if (!cfg.getIsCode() || !cm.isCode && !cm.isAutoMode) {
                throw new SaOAuth2Exception("暂未开放的授权模式");
            } else {
                return SaOAuth2Handle.authorize(req, res, cfg);
            }
        } else if (req.isPath(SaOAuth2Consts.Api.token) && req.isParam(SaOAuth2Consts.Param.grant_type, SaOAuth2Consts.GrantType.authorization_code)) {
            return SaOAuth2Handle.token(req, res, cfg);
        } else if (req.isPath(SaOAuth2Consts.Api.token) && req.isParam(SaOAuth2Consts.Param.grant_type, SaOAuth2Consts.GrantType.refresh_token)) {
            return SaOAuth2Handle.refreshToken(req);
        } else if (req.isPath(SaOAuth2Consts.Api.revoke)) {
            return SaOAuth2Handle.revokeToken(req);
        } else if (req.isPath(SaOAuth2Consts.Api.doLogin)) {
            return SaOAuth2Handle.doLogin(req, res, cfg);
        } else if (req.isPath(SaOAuth2Consts.Api.doConfirm)) {
            return SaOAuth2Handle.doConfirm(req);
        } else if (req.isPath(SaOAuth2Consts.Api.authorize) && req.isParam(SaOAuth2Consts.Param.response_type, SaOAuth2Consts.ResponseType.token)) {
            cm = SaOAuth2Handle.currClientModel();
            if (!cfg.getIsImplicit() || !cm.isImplicit && !cm.isAutoMode) {
                throw new SaOAuth2Exception("暂未开放的授权模式");
            } else {
                return SaOAuth2Handle.authorize(req, res, cfg);
            }
        } else if (req.isPath(SaOAuth2Consts.Api.token) && req.isParam(SaOAuth2Consts.Param.grant_type, SaOAuth2Consts.GrantType.password)) {
            cm = SaOAuth2Handle.currClientModel();
            if (!cfg.getIsPassword() || !cm.isPassword && !cm.isAutoMode) {
                throw new SaOAuth2Exception("暂未开放的授权模式");
            } else {
                return SaOAuth2Handle.password(req, res, cfg);
            }
        } else if (req.isPath(SaOAuth2Consts.Api.token) && req.isParam(SaOAuth2Consts.Param.grant_type, SaOAuth2Consts.GrantType.client_credentials)) {
            cm = SaOAuth2Handle.currClientModel();
            if (!cfg.getIsClient() || !cm.isClient && !cm.isAutoMode) {
                throw new SaOAuth2Exception("暂未开放的授权模式");
            } else {
                return SaOAuth2Handle.clientToken(req, res, cfg);
            }
        } else {
            return "{\"msg\": \"not handle\"}";
        }
    }
}
