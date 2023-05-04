/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller;

import cc.iotkit.common.Constants;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.data.IHomeData;
import cc.iotkit.data.ISpaceData;
import cc.iotkit.data.IUserInfoData;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.space.Home;
import cc.iotkit.model.space.Space;
import cc.iotkit.utils.AuthUtil;
import cn.dev33.satoken.annotation.SaCheckRole;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Api(tags = {"用户"})
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private IUserInfoData userInfoData;
    @Autowired
    private DataOwnerService ownerService;
    @Autowired
    private IHomeData homeData;
    @Autowired
    private ISpaceData spaceData;

    /**
     * 平台用户列表
     */
    @SaCheckRole("iot_admin")
    @GetMapping("/platform/users")
    public List<UserInfo> getPlatformUsers() {
        return userInfoData.findByType(UserInfo.USER_TYPE_PLATFORM);
    }

    /**
     * 添加平台用户
     */
    @SaCheckRole("iot_admin")
    @PostMapping("/platform/user/add")
    public void addPlatformUser(@RequestBody UserInfo user) {
        try {
            user.setId(UUID.randomUUID().toString());
            user.setType(UserInfo.USER_TYPE_PLATFORM);
            user.setOwnerId(AuthUtil.getUserId());
            user.setRoles(Collections.singletonList(Constants.ROLE_SYSTEM));
            user.setPermissions(Collections.singletonList(Constants.PERMISSION_WRITE));
            user.setCreateAt(System.currentTimeMillis());
            user.setSecret(AuthUtil.enCryptPwd(Constants.PWD_SYSTEM_USER));
            userInfoData.save(user);
        } catch (Throwable e) {
            throw new BizException(ErrCode.ADD_PLATFORM_USER_ERROR, e);
        }
    }

    /**
     * 重置平台用户密码
     */
    @SaCheckRole("iot_admin")
    @PostMapping("/platform/user/{uid}/resetPwd")
    public void resetPlatformUserPwd(@PathVariable("uid") String uid) {
        try {
            UserInfo user = userInfoData.findByUid(uid);
            if (user == null) {
                throw new BizException(ErrCode.USER_NOT_FOUND);
            }
            user.setSecret(AuthUtil.enCryptPwd(Constants.PWD_SYSTEM_USER));
            userInfoData.save(user);
        } catch (Throwable e) {
            throw new BizException(ErrCode.RESET_PWD_ERROR, e);
        }
    }

    /**
     * 客户端用户列表
     */
    @GetMapping("/client/users")
    public List<UserInfo> clientUsers() {
        return userInfoData.findByTypeAndOwnerId(UserInfo.USER_TYPE_CLIENT, AuthUtil.getUserId());
    }

    /**
     * 添加C端用户
     */
    @PostMapping("/client/user/add")
    public void addClientUser(@RequestBody UserInfo user) throws Exception {
        user.setType(UserInfo.USER_TYPE_CLIENT);
        user.setOwnerId(AuthUtil.getUserId());
        user.setRoles(Collections.singletonList(Constants.ROLE_CLIENT));
        user.setCreateAt(System.currentTimeMillis());
        user.setSecret(AuthUtil.enCryptPwd(Constants.PWD_CLIENT_USER));
        user = userInfoData.save(user);

        //添加默认家庭
        Home home = homeData.save(Home.builder()
                .name("我的家庭")
                .address("")
                .deviceNum(0)
                .spaceNum(0)
                .uid(user.getId())
                .current(true)
                .build());

        //添加默认房间
        for (String name : new String[]{"客厅", "卧室", "厨房"}) {
            spaceData.save(Space.builder()
                    .homeId(home.getId())
                    .name(name)
                    .uid(user.getId())
                    .createAt(System.currentTimeMillis())
                    .build());
        }
    }

    @PostMapping("/client/user/{id}/delete")
    public void deleteClientUser(@PathVariable("id") String id) {
        UserInfo user = userInfoData.findById(id);
        if (user == null) {
            throw new BizException(ErrCode.USER_NOT_FOUND);
        }
        userInfoData.deleteById(id);
    }

    @PostMapping("/client/user/save")
    public void saveClientUser(@RequestBody UserInfo user) {
        UserInfo oldUser = userInfoData.findById(user.getId());
        if (oldUser == null) {
            return;
        }
        if (!AuthUtil.getUserId().equals(oldUser.getOwnerId())) {
            throw new BizException(ErrCode.UNAUTHORIZED_EXCEPTION);
        }
        ReflectUtil.copyNoNulls(user, oldUser);
        userInfoData.save(oldUser);
    }

    /**
     * 修改密码
     */
    @PostMapping("/{uid}/modifyPwd")
    public void modifyPwd(@PathVariable("uid") String uid, String oldPwd, String newPwd) {
        UserInfo user = userInfoData.findByUid(uid);
        if (user == null) {
            throw new BizException(ErrCode.USER_NOT_FOUND);
        }
        if (!AuthUtil.getUserId().equals(user.getId())) {
            throw new BizException(ErrCode.UNAUTHORIZED_EXCEPTION);
        }

        try {
            if (!AuthUtil.checkPwd(oldPwd, user.getSecret())) {
                throw new BizException(ErrCode.PWD_ERROR);
            }

            user.setSecret(AuthUtil.enCryptPwd(newPwd));
            userInfoData.save(user);
        } catch (BizException e) {
            throw e;
        } catch (Throwable e) {
            throw new BizException(ErrCode.UPDATE_PWD_ERROR, e);
        }
    }

}
