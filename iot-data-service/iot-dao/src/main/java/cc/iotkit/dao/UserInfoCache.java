package cc.iotkit.dao;

import cc.iotkit.common.Constants;
import cc.iotkit.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class UserInfoCache {

    @Autowired
    private UserInfoRepository userInfoRepository;

    private static UserInfoCache INSTANCE;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public static UserInfoCache getInstance() {
        return INSTANCE;
    }

    @Cacheable(value = Constants.USER_CACHE, key = "#uid")
    public UserInfo getUserInfo(String uid) {
        return userInfoRepository.findById(uid).orElse(null);
    }

}
