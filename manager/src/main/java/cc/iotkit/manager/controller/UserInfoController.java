package cc.iotkit.manager.controller;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.manager.config.Constants;
import cc.iotkit.manager.service.AligenieService;
import cc.iotkit.manager.service.KeycloakAdminService;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserInfoController extends DbBaseController<UserInfoRepository, UserInfo> {

    @Value("${app.systemRole}")
    private String systemRole;

    private final KeycloakAdminService keycloakAdminService;
    private final UserInfoRepository userInfoRepository;
    private final AligenieService aligenieService;

    @Autowired
    public UserInfoController(UserInfoRepository userInfoRepository,
                              KeycloakAdminService keycloakAdminService,
                              AligenieService aligenieService) {
        super(userInfoRepository);
        this.keycloakAdminService = keycloakAdminService;
        this.userInfoRepository = userInfoRepository;
        this.aligenieService = aligenieService;
    }

    /**
     * 平台用户列表
     */
    @PreAuthorize("hasRole('iot_admin')")
    @GetMapping("/platform/users")
    public List<UserInfo> getPlatformUsers() {
        return userInfoRepository.findAll(Example.of(UserInfo.builder()
                .type(UserInfo.USER_TYPE_PLATFORM).build()));
    }

    /**
     * 添加平台用户
     */
    @PostMapping("/platform/user/add")
    public void addPlatformUser(@RequestBody UserInfo user) {
        user.setId(UUID.randomUUID().toString());
        user.setType(UserInfo.USER_TYPE_PLATFORM);
        user.setOwnerId(AuthUtil.getUserId());
        user.setRoles(Arrays.asList(Constants.ROLE_SYSTEM));
        user.setCreateAt(System.currentTimeMillis());
        keycloakAdminService.createUser(user, Constants.PWD_SYSTEM_USER);
        userInfoRepository.save(user);
    }

    /**
     * 客户端用户列表
     */
    @GetMapping("/client/users")
    public List<UserInfo> clientUsers() {
        return userInfoRepository.findAll(Example.of(
                UserInfo.builder()
                        .type(UserInfo.USER_TYPE_CLIENT)
                        .ownerId(AuthUtil.getUserId())
                        .build()));
    }

    /**
     * 添加C端用户
     */
    @PostMapping("/client/user/add")
    public void addClientUser(@RequestBody UserInfo user) {
        user.setId(UUID.randomUUID().toString());
        user.setType(UserInfo.USER_TYPE_CLIENT);
        user.setOwnerId(AuthUtil.getUserId());
        user.setRoles(Arrays.asList(Constants.ROLE_CLIENT));
        user.setCreateAt(System.currentTimeMillis());
        keycloakAdminService.createUser(user, Constants.PWD_CLIENT_USER);
        userInfoRepository.save(user);
    }

    @PostMapping("/client/user/save")
    public void saveClientUser(@RequestBody UserInfo user) {
        Optional<UserInfo> userOpt = userInfoRepository.findById(user.getId());
        if (!userOpt.isPresent()) {
            return;
        }
        UserInfo oldUser = userOpt.get();
        if (!AuthUtil.getUserId().equals(oldUser.getOwnerId())) {
            throw new BizException("无权限操作");
        }
        ReflectUtil.copyNoNulls(user, oldUser);
        userInfoRepository.save(oldUser);

        boolean isAligenie = user.getUsePlatforms().isAligenie();
        //同步天猫精灵设备
        if (oldUser.getUsePlatforms().isAligenie() != isAligenie) {
            aligenieService.syncDevice(user);
        }
    }
}
