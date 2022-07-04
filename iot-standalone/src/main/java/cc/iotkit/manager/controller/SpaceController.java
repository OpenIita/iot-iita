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

import cc.iotkit.common.exception.BizException;
import cc.iotkit.dao.HomeRepository;
import cc.iotkit.dao.SpaceRepository;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.utils.AuthUtil;
import cc.iotkit.model.space.Home;
import cc.iotkit.model.space.Space;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/space")
public class SpaceController {

    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private HomeRepository homeRepository;
    @Autowired
    private DataOwnerService dataOwnerService;

    /**
     * 取用户当前家庭
     */
    @GetMapping("/currentHome")
    public Home getCurrentHome() {
        return homeRepository.findByUidAndCurrent(AuthUtil.getUserId(), true);
    }

    /**
     * 保存家庭信息
     */
    @PostMapping("/saveHome/{id}")
    public void saveHome(@PathVariable("id") String id, Home home) {
        Optional<Home> optHome = homeRepository.findById(id);
        if (!optHome.isPresent()) {
            throw new BizException("home does not exist");
        }
        Home oldHome = optHome.get();
        dataOwnerService.checkOwner(oldHome);
        if (StringUtils.isNotBlank(home.getName())) {
            oldHome.setName(home.getName());
        }
        if (StringUtils.isNotBlank(home.getAddress())) {
            oldHome.setName(home.getAddress());
        }
        homeRepository.save(oldHome);
    }

    /**
     * 我的空间列表
     */
    @GetMapping("/spaces/{homeId}")
    public List<Space> getSpaces(@PathVariable("homeId") String homeId) {
        return spaceRepository.findByUidAndHomeIdOrderByCreateAtDesc(AuthUtil.getUserId(), homeId);
    }

    /**
     * 在当前家庭中添加空间
     */
    @PostMapping("/addSpace")
    public void addSpace(String name) {
        String uid = AuthUtil.getUserId();
        Home currHome = homeRepository.findByUidAndCurrent(uid, true);
        if (currHome == null) {
            throw new BizException("current home does not exist");
        }
        spaceRepository.save(Space.builder()
                .homeId(currHome.getId())
                .name(name)
                .uid(uid)
                .createAt(System.currentTimeMillis())
                .build());
    }

    @DeleteMapping("/delSpace/{id}")
    public void delSpace(@PathVariable("id") String id) {
        checkExistAndOwner(id);
        spaceRepository.deleteById(id);
    }

    @PostMapping("/saveSpace/{id}")
    public void saveSpace(@PathVariable("id") String id, String name) {
        Space oldSpace = checkExistAndOwner(id);
        oldSpace.setName(name);
        spaceRepository.save(oldSpace);
    }

    private Space checkExistAndOwner(String id) {
        Optional<Space> optSpace = spaceRepository.findById(id);
        if (!optSpace.isPresent()) {
            throw new BizException("space does not exist");
        }

        dataOwnerService.checkOwner(optSpace.get());
        return optSpace.get();
    }

}
