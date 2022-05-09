package cc.iotkit.manager.controller;

import cc.iotkit.dao.*;
import cc.iotkit.manager.model.vo.SpaceDeviceVo;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.space.Space;
import cc.iotkit.model.space.SpaceDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/space")
public class SpaceDeviceController {

    @Autowired
    private SpaceDeviceRepository spaceDeviceRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceCache deviceCache;
    @Autowired
    private ProductCache productCache;
    @Autowired
    private SpaceCache spaceCache;

    /**
     * 我最近使用的设备列表
     */
    @GetMapping("/myRecentDevices")
    public List<SpaceDeviceVo> getMyRecentDevices() {
        List<SpaceDevice> spaceDevices = spaceDeviceRepository.findByUidOrderByUseAtDesc(AuthUtil.getUserId());
        return spaceDevices.stream().map((this::parseSpaceDevice)).collect(Collectors.toList());
    }

    /**
     * 我的空间设备列表-按空间获取
     *
     * @param spaceId 空间id
     */
    @GetMapping("/myDevices/{spaceId}")
    public List<SpaceDeviceVo> getMyDevices(String spaceId) {
        List<SpaceDevice> spaceDevices = spaceDeviceRepository.findByUidOrderByUseAtDesc(AuthUtil.getUserId());
        return spaceDevices.stream().map((this::parseSpaceDevice)).collect(Collectors.toList());
    }


    private SpaceDeviceVo parseSpaceDevice(SpaceDevice sd) {
        DeviceInfo device = deviceCache.get(sd.getDeviceId());
        Space space = spaceCache.getSpace(sd.getSpaceId());
        Product product = productCache.findById(device.getProductKey());
        DeviceInfo.State state = device.getState();

        return SpaceDeviceVo.builder()
                .id(sd.getId())
                .deviceId(sd.getDeviceId())
                .deviceName(device.getDeviceName())
                .name(sd.getName())
                .spaceId(sd.getSpaceId())
                .spaceName(space.getName())
                .productKey(device.getProductKey())
                .picUrl(product.getImg())
                .online(state != null && state.isOnline())
                .property(device.getProperty())
                .uid(sd.getUid())
                .build();
    }


//    @PostMapping("/list")
//    public Paging<SpaceInfo> getDevices(int page,
//                                            int limit,
//                                            String address) {
//        Criteria condition = new Criteria();
//        if (StringUtils.isNotBlank(address)) {
//            condition.and("address").regex(".*" + address + ".*");
//        }
//        List<UserInfo> userInfoList = userInfoDao.find(condition, (page - 1) * limit,
//                limit, Sort.Order.desc("createAt"));
//
//        List<SpaceInfo> spaces = userInfoList.stream().map((u ->
//                new SpaceInfo(u.getAddress(), u.getUid())))
//                .collect(Collectors.toList());
//
//        return new Paging<>(userInfoDao.count(condition),
//                spaces);
//    }

    @GetMapping("/{userId}/devices")
    public List<SpaceDeviceVo> getDevices(@PathVariable("userId") String userId) {
        List<SpaceDeviceVo> deviceVos = new ArrayList<>();
        List<SpaceDevice> devices = spaceDeviceRepository.findAll(Example.of(SpaceDevice.builder().uid(userId).build()));
        devices.forEach(sd -> {
            DeviceInfo deviceInfo = deviceCache.get(sd.getDeviceId());
            Product product = productCache.findById(deviceInfo.getProductKey());
            deviceVos.add(SpaceDeviceVo.builder()
                    .deviceId(sd.getDeviceId())
                    .name(sd.getName())
                    .picUrl(product.getImg())
                    .spaceName("")
                    .online(deviceInfo.getState().isOnline())
                    .property(deviceInfo.getProperty())
                    .productKey(deviceInfo.getProductKey())
                    .build());
        });
        return deviceVos;
    }

}
