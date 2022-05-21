package cc.iotkit.manager.controller.api;

import cc.iotkit.dao.AppInfoRepository;
import cc.iotkit.dao.HomeRepository;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.utils.AuthUtil;
import cc.iotkit.model.AppInfo;
import cc.iotkit.model.space.Home;
import cc.iotkit.model.UserInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("api-sys")
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private HomeRepository homeRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private AppInfoRepository appInfoRepository;

    @ApiOperation("设置当前家庭")
    @PostMapping("/setHomeId")
    public void setHomeId(String homeId) {
        if (StringUtils.isBlank(homeId)) {
            throw new RuntimeException("homeId is blank.");
        }
        Home home = homeRepository.findById(homeId).orElseThrow(() -> new RuntimeException("home is not found."));
        String uid = AuthUtil.getUserId();
        if (!uid.equals(home.getUid())) {
            throw new RuntimeException("用户不属于该家庭");
        }

        UserInfo userInfo = userInfoRepository.findOne(Example.of(UserInfo.builder().uid(uid).build()))
                .orElseThrow(() -> new RuntimeException(("用户信息不存在")));
        userInfo.setCurrHomeId(homeId);
        userInfoRepository.save(userInfo);
    }

    @GetMapping("/getUserInfo")
    public UserInfo getUserInfo() {
        return userInfoRepository.findById(AuthUtil.getUserId()).orElseThrow(() ->
                new RuntimeException("用户不存在")
        );
    }

    @GetMapping("/getAppInfo")
    public AppInfo getAppInfo() {
        return appInfoRepository.findAll().get(0);
    }
}
