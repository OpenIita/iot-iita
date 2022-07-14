package cc.iotkit.data;

import cc.iotkit.model.UserInfo;

import java.util.List;

public interface IUserInfoData extends ICommonData<UserInfo, String> {

    UserInfo findByUid(String uid);

    List<UserInfo> findByType(int type);

    List<UserInfo> findByTypeAndOwnerId(int type, String ownerId);
}
