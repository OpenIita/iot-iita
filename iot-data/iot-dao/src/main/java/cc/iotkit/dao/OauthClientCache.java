/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.dao;

import cc.iotkit.common.Constants;
import cc.iotkit.data.IOauthClientData;
import cc.iotkit.model.OauthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class OauthClientCache {

    @Autowired
    private IOauthClientData oauthClientData;

    private static OauthClientCache INSTANCE;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public static OauthClientCache getInstance() {
        return INSTANCE;
    }

    @Cacheable(value = Constants.OAUTH_CLIENT_CACHE, key = "#clientId")
    public OauthClient getClient(String clientId) {
        return oauthClientData.findByClientId(clientId);
    }

}
