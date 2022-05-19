package cc.iotkit.dao;

import cc.iotkit.common.Constants;
import cc.iotkit.model.OauthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class OauthClientCache {

    @Autowired
    private OauthClientRepository oauthClientRepository;

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
        return oauthClientRepository.findById(clientId).orElse(null);
    }

}
