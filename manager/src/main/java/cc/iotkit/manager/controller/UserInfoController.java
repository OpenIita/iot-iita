package cc.iotkit.manager.controller;

import cc.iotkit.common.Constants;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.dao.AligenieDeviceRepository;
import cc.iotkit.dao.HomeRepository;
import cc.iotkit.dao.SpaceRepository;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.service.PulsarAdminService;
import cc.iotkit.model.space.Home;
import cc.iotkit.model.space.Space;
import cc.iotkit.utils.AuthUtil;
import cc.iotkit.model.UserInfo;
import cn.dev33.satoken.annotation.SaCheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PulsarAdminService pulsarAdminService;
    @Autowired
    private AligenieDeviceRepository aligenieDeviceRepository;
    @Autowired
    private DataOwnerService ownerService;
    @Autowired
    private HomeRepository homeRepository;
    @Autowired
    private SpaceRepository spaceRepository;


    /**
     * 平台用户列表
     */
    @SaCheckRole("iot_admin")
    @GetMapping("/platform/users")
    public List<UserInfo> getPlatformUsers() {
        return userInfoRepository.findByType(UserInfo.USER_TYPE_PLATFORM);
    }

    /**
     * 添加平台用户
     */
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
            userInfoRepository.save(user);
        } catch (Throwable e) {
            throw new BizException("add platform user error", e);
        }
    }

    /**
     * 客户端用户列表
     */
    @GetMapping("/client/users")
    public List<UserInfo> clientUsers() {
        return userInfoRepository.findByTypeAndOwnerId(UserInfo.USER_TYPE_CLIENT, AuthUtil.getUserId());
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
        user = userInfoRepository.save(user);

        //添加默认家庭
        Home home = homeRepository.save(Home.builder()
                .name("我的家庭")
                .address("")
                .deviceNum(0)
                .spaceNum(0)
                .uid(user.getId())
                .current(true)
                .build());

        //添加默认房间
        for (String name : new String[]{"客厅", "卧室", "厨房"}) {
            spaceRepository.save(Space.builder()
                    .homeId(home.getId())
                    .name(name)
                    .uid(user.getId())
                    .createAt(System.currentTimeMillis())
                    .build());
        }
    }

    @PostMapping("/client/user/{id}/delete")
    public void deleteClientUser(@PathVariable("id") String id) {
        Optional<UserInfo> optUser = userInfoRepository.findById(id);
        if (optUser.isEmpty()) {
            throw new BizException("user does not exist");
        }
        UserInfo user = optUser.get();
        ownerService.checkOwner(user);
        userInfoRepository.deleteById(id);
        aligenieDeviceRepository.deleteByUid(user.getId());
    }

    @PostMapping("/client/user/save")
    public void saveClientUser(@RequestBody UserInfo user) {
        Optional<UserInfo> userOpt = userInfoRepository.findById(user.getId());
        if (userOpt.isEmpty()) {
            return;
        }
        UserInfo oldUser = userOpt.get();
        if (!AuthUtil.getUserId().equals(oldUser.getOwnerId())) {
            throw new BizException("无权限操作");
        }
        ReflectUtil.copyNoNulls(user, oldUser);
        userInfoRepository.save(oldUser);
    }
}
