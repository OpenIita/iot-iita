package cc.iotkit.data.service;

import cc.iotkit.common.constant.Constants;
import cc.iotkit.data.manager.IUserInfoData;
import cc.iotkit.data.cache.UserInfoCacheEvict;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("userInfoDataCache")
public class UserInfoDataCache implements IUserInfoData {

    @Autowired
    private IUserInfoData userInfoData;
    @Autowired
    private UserInfoCacheEvict userInfoCacheEvict;

    @Override
    @Cacheable(value = Constants.CACHE_USER_INFO, key = "#root.method.name+#uid", unless = "#result == null")
    public UserInfo findByUid(String uid) {
        return userInfoData.findByUid(uid);
    }

    @Override
    public List<UserInfo> findByType(int type) {
        return userInfoData.findByType(type);
    }

    @Override
    public List<UserInfo> findByTypeAndOwnerId(int type, String ownerId) {
        return userInfoData.findByTypeAndOwnerId(type, ownerId);
    }

    @Override
    public UserInfo findById(String s) {
        return userInfoData.findById(s);
    }

    @Override
    public UserInfo save(UserInfo data) {
        UserInfo userInfo = userInfoData.save(data);
        //清除缓存
        userInfoCacheEvict.findByUid(data.getUid());
        return userInfo;
    }

    @Override
    public UserInfo add(UserInfo data) {
        return userInfoData.add(data);
    }

    @Override
    public void deleteById(String s) {
        userInfoData.deleteById(s);
    }

    @Override
    public void deleteByIds(String[] strings) {

    }

    @Override
    public long count() {
        return userInfoData.count();
    }

    @Override
    public List<UserInfo> findAll() {
        return userInfoData.findAll();
    }

    @Override
    public Paging<UserInfo> findAll(int page, int size) {
        return userInfoData.findAll(page, size);
    }
}
