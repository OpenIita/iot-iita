package cc.iotkit.manager.controller;

import cc.iotkit.common.Constants;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.manager.service.AligenieService;
import cc.iotkit.manager.service.KeycloakAdminService;
import cc.iotkit.manager.service.PulsarAdminService;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final PulsarAdminService pulsarAdminService;

    @Autowired
    public UserInfoController(UserInfoRepository userInfoRepository,
                              KeycloakAdminService keycloakAdminService,
                              AligenieService aligenieService,
                              PulsarAdminService pulsarAdminService) {
        super(userInfoRepository);
        this.keycloakAdminService = keycloakAdminService;
        this.userInfoRepository = userInfoRepository;
        this.aligenieService = aligenieService;
        this.pulsarAdminService = pulsarAdminService;
    }

    /**
     * 平台用户列表
     */
    @PreAuthorize("hasRole('iot_admin')")
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
            user.setRoles(Arrays.asList(Constants.ROLE_SYSTEM));
            user.setCreateAt(System.currentTimeMillis());
            UserInfo keycloakUser = keycloakAdminService.getUser(user.getUid());
            if (keycloakUser != null) {
                user.setId(keycloakUser.getId());
                keycloakAdminService.updateUser(user);
            } else {
                keycloakAdminService.createUser(user, Constants.PWD_SYSTEM_USER);
            }
            if (!pulsarAdminService.tenantExists(user.getUid())) {
                pulsarAdminService.createTenant(user.getUid());
            }
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
