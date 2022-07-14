package cc.iotkit.data.service;

import cc.iotkit.data.IUserInfoData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoDataImpl implements IUserInfoData {
    @Override
    public UserInfo findByUid(String uid) {
        return null;
    }

    @Override
    public List<UserInfo> findByType(int type) {
        return null;
    }

    @Override
    public List<UserInfo> findByTypeAndOwnerId(int type, String ownerId) {
        return null;
    }

    @Override
    public UserInfo findById(String s) {
        return null;
    }

    @Override
    public UserInfo save(UserInfo data) {
        return null;
    }

    @Override
    public UserInfo add(UserInfo data) {
        return null;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<UserInfo> findAll() {
        return null;
    }

    @Override
    public Paging<UserInfo> findAll(int page, int size) {
        return null;
    }
}
