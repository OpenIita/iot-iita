package cc.iotkit.oauth.service;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.oauth2.SaOAuth2Manager;
import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.dev33.satoken.oauth2.exception.SaOAuth2Exception;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Handle;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.oauth2.model.AccessTokenModel;
import cn.dev33.satoken.oauth2.model.ClientTokenModel;
import cn.dev33.satoken.oauth2.model.RequestAuthModel;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;

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
            return token(req, res, cfg);
        } else if (req.isPath(SaOAuth2Consts.Api.token) && req.isParam(SaOAuth2Consts.Param.grant_type, SaOAuth2Consts.GrantType.refresh_token)) {
            return refreshToken(req);
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
                return password(req, res, cfg);
            }
        } else if (req.isPath(SaOAuth2Consts.Api.token) && req.isParam(SaOAuth2Consts.Param.grant_type, SaOAuth2Consts.GrantType.client_credentials)) {
            cm = SaOAuth2Handle.currClientModel();
            if (!cfg.getIsClient() || !cm.isClient && !cm.isAutoMode) {
                throw new SaOAuth2Exception("暂未开放的授权模式");
            } else {
                return clientToken(req, res, cfg);
            }
        } else {
            return "{\"msg\": \"not handle\"}";
        }
    }

    public static Object token(SaRequest req, SaResponse res, SaOAuth2Config cfg) {
        String code = req.getParamNotNull(SaOAuth2Consts.Param.code);
        String clientId = req.getParamNotNull(SaOAuth2Consts.Param.client_id);
        String clientSecret = req.getParamNotNull(SaOAuth2Consts.Param.client_secret);
        String redirectUri = req.getParam(SaOAuth2Consts.Param.redirect_uri);
        SaOAuth2Util.checkGainTokenParam(code, clientId, clientSecret, redirectUri);
        AccessTokenModel token = SaOAuth2Util.generateAccessToken(code);
        return token.toLineMap();
    }

    public static Object refreshToken(SaRequest req) {
        String clientId = req.getParamNotNull(SaOAuth2Consts.Param.client_id);
        String clientSecret = req.getParamNotNull(SaOAuth2Consts.Param.client_secret);
        String refreshToken = req.getParamNotNull(SaOAuth2Consts.Param.refresh_token);
        SaOAuth2Util.checkRefreshTokenParam(clientId, clientSecret, refreshToken);
        return SaOAuth2Util.refreshAccessToken(refreshToken).toLineMap();
    }

    public static Object password(SaRequest req, SaResponse res, SaOAuth2Config cfg) {
        String username = req.getParamNotNull(SaOAuth2Consts.Param.username);
        String password = req.getParamNotNull(SaOAuth2Consts.Param.password);
        String clientId = req.getParamNotNull(SaOAuth2Consts.Param.client_id);
        String scope = req.getParam(SaOAuth2Consts.Param.scope, "");
        SaOAuth2Util.checkContract(clientId, scope);
        SaHolder.getStorage().set(StpUtil.stpLogic.splicingKeyJustCreatedSave(), "no-token");
        Object retObj = cfg.getDoLoginHandle().apply(username, password);
        if (!StpUtil.isLogin()) {
            return retObj;
        } else {
            RequestAuthModel ra = new RequestAuthModel();
            ra.clientId = clientId;
            ra.loginId = StpUtil.getLoginId();
            ra.scope = scope;
            AccessTokenModel at = SaOAuth2Util.generateAccessToken(ra, true);
            return at.toLineMap();
        }
    }

    public static Object clientToken(SaRequest req, SaResponse res, SaOAuth2Config cfg) {
        String clientId = req.getParamNotNull(SaOAuth2Consts.Param.client_id);
        String clientSecret = req.getParamNotNull(SaOAuth2Consts.Param.client_secret);
        String scope = req.getParam(SaOAuth2Consts.Param.scope);
        SaOAuth2Util.checkContract(clientId, scope);
        SaOAuth2Util.checkClientSecret(clientId, clientSecret);
        ClientTokenModel ct = SaOAuth2Util.generateClientToken(clientId, scope);
        return ct.toLineMap();
    }
}
