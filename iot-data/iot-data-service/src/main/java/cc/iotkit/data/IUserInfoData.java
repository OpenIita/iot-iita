/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data;

import cc.iotkit.model.UserInfo;

import java.util.List;

public interface IUserInfoData extends ICommonData<UserInfo, String> {

    UserInfo findByUid(String uid);

    List<UserInfo> findByType(int type);

    List<UserInfo> findByTypeAndOwnerId(int type, String ownerId);
}
