/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller.api;

import cc.iotkit.dao.HomeRepository;
import cc.iotkit.dao.SpaceRepository;
import cc.iotkit.dao.UserActionLogRepository;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.model.UserActionLog;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.space.Home;
import cc.iotkit.model.space.Space;
import cc.iotkit.utils.AuthUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("api-home")
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private HomeRepository homeRepository;
    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserActionLogRepository userActionLogRepository;

    public HomeController() {
    }

    @PostMapping("/add")
    public void add(String name, String address) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(address)) {
            throw new RuntimeException("name/address is blank.");
        }
        Home home = homeRepository.save(Home.builder().name(name)
                .address(address).uid(AuthUtil.getUserId()).build());

        UserInfo userInfo = userInfoRepository.findById(AuthUtil.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        userInfo.setCurrHomeId(home.getId());
        userInfoRepository.save(userInfo);
    }

    @GetMapping("/list")
    public List<Home> list() {
        return homeRepository.findByUid(AuthUtil.getUserId());
    }

    @PostMapping("/addSpace")
    public void addSpace(String homeId, String name) {
        if (StringUtils.isBlank(homeId) || StringUtils.isBlank(name)) {
            throw new RuntimeException("name/homeId is blank.");
        }
        String uid = AuthUtil.getUserId();
        Home home = homeRepository.findByUidAndId(uid, homeId);
        if (home == null) {
            throw new RuntimeException("用户家庭不存在");
        }

        Space s = spaceRepository.save(Space.builder()
                .name(name)
                .homeId(home.getId())
                .uid(home.getUid())
                .build());

        //记录用户操作日志
        userActionLogRepository.save(UserActionLog.builder()
                .uid(uid)
                .type(UserActionLog.Type.SPACE_ADD.getValue())
                .createAt(System.currentTimeMillis())
                .log(s)
                .build());

    }

    @PostMapping("/updateSpace")
    public void updateSpace(String spaceId, String name) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(spaceId)) {
            throw new RuntimeException("name/spaceId is blank.");
        }
        Space space = spaceRepository.findById(spaceId).orElseThrow(() -> new RuntimeException("space not found"));
        space.setName(name);
        spaceRepository.save(space);
    }

    @PostMapping("/spaces")
    public List<Space> spaces(String homeId) {
        if (StringUtils.isBlank(homeId)) {
            throw new RuntimeException("homeId is blank.");
        }
        return spaceRepository.findByHomeId(homeId);
    }

    @GetMapping("/getCurrentHome")
    public Home getCurrentHome() {
        UserInfo userInfo = userInfoRepository.findById(AuthUtil.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        if (StringUtils.isBlank(userInfo.getCurrHomeId())) {
            throw new RuntimeException("还未创建家庭");
        }
        return homeRepository.findById(userInfo.getCurrHomeId())
                .orElseThrow(() -> new RuntimeException("房间不存在"));
    }

}
