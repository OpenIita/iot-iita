package cc.iotkit.manager.controller.api;

import cc.iotkit.dao.HomeRepository;
import cc.iotkit.dao.SpaceRepository;
import cc.iotkit.dao.UserActionLogRepository;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.space.Home;
import cc.iotkit.model.space.Space;
import cc.iotkit.model.UserActionLog;
import cc.iotkit.model.UserInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("添加家庭")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "名称", name = "name", required = true, dataType = "String"),
            @ApiImplicitParam(value = "地址", name = "address", required = true, dataType = "String"),
    })
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

    @ApiOperation("我家庭列表")
    @GetMapping("/list")
    public List<Home> list() {
//        return homeRepository.findAll();
        return homeRepository.findAll(Example.of(Home.builder()
                .uid(AuthUtil.getUserId()).build()));
    }

    @ApiOperation("添加空间")
    @PostMapping("/addSpace")
    public void addSpace(String homeId, String name) {
        if (StringUtils.isBlank(homeId) || StringUtils.isBlank(name)) {
            throw new RuntimeException("name/homeId is blank.");
        }
        String uid = AuthUtil.getUserId();
        Home home = homeRepository.findOne(Example.of(Home.builder().uid(uid).id(homeId).build()))
                .orElseThrow(() -> new RuntimeException("用户家庭不存在"));

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

    @ApiOperation("修改空间")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "空间id", name = "spaceId", required = true, dataType = "String"),
            @ApiImplicitParam(value = "空间名称", name = "name", required = true, dataType = "String"),
    })
    @PostMapping("/updateSpace")
    public void updateSpace(String spaceId, String name) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(spaceId)) {
            throw new RuntimeException("name/spaceId is blank.");
        }
        Space space = spaceRepository.findById(spaceId).orElseThrow(() -> new RuntimeException("space not found"));
        space.setName(name);
        spaceRepository.save(space);
    }

    @ApiOperation("空间列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "家庭id", name = "homeId", required = true, dataType = "String"),
    })
    @PostMapping("/spaces")
    public List<Space> spaces(String homeId) {
        if (StringUtils.isBlank(homeId)) {
            throw new RuntimeException("homeId is blank.");
        }
//        return spaceRepository.findAll();
        return spaceRepository.findAll(Example.of(Space.builder().homeId(homeId).build()));
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
