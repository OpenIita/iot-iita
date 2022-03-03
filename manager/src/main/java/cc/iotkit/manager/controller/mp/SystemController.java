package cc.iotkit.manager.controller.mp;

import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.manager.model.vo.LoginResult;
import cc.iotkit.manager.service.WeChatService;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.UserInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("mp-sys")
@RequestMapping("/mp/sys")
public class SystemController {

    @Autowired
    private WeChatService weChatService;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @ApiOperation("用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "加密的用户信息", name = "userInfo", required = true, dataType = "String"),
            @ApiImplicitParam(value = "加密向量", name = "iv", required = true, dataType = "String"),
            @ApiImplicitParam(value = "登录码", name = "loginCode", required = true, dataType = "String"),
    })
    @PostMapping("/login")
    public LoginResult login(String userInfo, String iv, String loginCode) {
        return new LoginResult(weChatService.login(userInfo, iv, loginCode));
    }

    @ApiOperation("用户设置")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "地址", name = "address", required = true, dataType = "String"),
    })
    @PostMapping("/settings")
    public void settings(String address) {
        UserInfo userInfo = userInfoRepository.findById(AuthUtil.getUserId()).get();
        userInfo.setAddress(address);
        userInfoRepository.save(userInfo);
    }

}
