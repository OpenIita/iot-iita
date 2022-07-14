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
import cc.iotkit.data.IUserInfoData;
import cc.iotkit.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class UserInfoCache {

    @Autowired
    private IUserInfoData userInfoData;

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
        return userInfoData.findById(uid);
    }

}
