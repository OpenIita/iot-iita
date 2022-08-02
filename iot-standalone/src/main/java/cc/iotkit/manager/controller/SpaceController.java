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
import cc.iotkit.data.IHomeData;
import cc.iotkit.data.ISpaceData;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.utils.AuthUtil;
import cc.iotkit.model.space.Home;
import cc.iotkit.model.space.Space;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/space")
public class SpaceController {

    @Autowired
    private ISpaceData spaceData;
    @Autowired
    private IHomeData homeData;
    @Autowired
    private DataOwnerService dataOwnerService;

    /**
     * 取用户当前家庭
     */
    @GetMapping("/currentHome")
    public Home getCurrentHome() {
        return homeData.findByUidAndCurrent(AuthUtil.getUserId(), true);
    }

    /**
     * 保存家庭信息
     */
    @PostMapping("/saveHome/{id}")
    public void saveHome(@PathVariable("id") String id, Home home) {
        Home oldHome = homeData.findById(id);
        if (home==null) {
            throw new BizException("home does not exist");
        }
        dataOwnerService.checkOwner(oldHome);
        if (StringUtils.isNotBlank(home.getName())) {
            oldHome.setName(home.getName());
        }
        if (StringUtils.isNotBlank(home.getAddress())) {
            oldHome.setName(home.getAddress());
        }
        homeData.save(oldHome);
    }

    /**
     * 我的空间列表
     */
    @GetMapping("/spaces/{homeId}")
    public List<Space> getSpaces(@PathVariable("homeId") String homeId) {
        return spaceData.findByUidAndHomeIdOrderByCreateAtDesc(AuthUtil.getUserId(), homeId);
    }

    /**
     * 在当前家庭中添加空间
     */
    @PostMapping("/addSpace")
    public void addSpace(String name) {
        String uid = AuthUtil.getUserId();
        Home currHome = homeData.findByUidAndCurrent(uid, true);
        if (currHome == null) {
            throw new BizException("current home does not exist");
        }
        spaceData.save(Space.builder()
                .homeId(currHome.getId())
                .name(name)
                .uid(uid)
                .createAt(System.currentTimeMillis())
                .build());
    }

    @DeleteMapping("/delSpace/{id}")
    public void delSpace(@PathVariable("id") String id) {
        checkExistAndOwner(id);
        spaceData.deleteById(id);
    }

    @PostMapping("/saveSpace/{id}")
    public void saveSpace(@PathVariable("id") String id, String name) {
        Space oldSpace = checkExistAndOwner(id);
        oldSpace.setName(name);
        spaceData.save(oldSpace);
    }

    private Space checkExistAndOwner(String id) {
        Space space = spaceData.findById(id);
        if (space == null) {
            throw new BizException("space does not exist");
        }

        dataOwnerService.checkOwner(space);
        return space;
    }

}
