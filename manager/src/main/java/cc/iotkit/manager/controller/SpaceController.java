package cc.iotkit.manager.controller;

import cc.iotkit.dao.*;
import cc.iotkit.manager.model.vo.SpaceDeviceVo;
import cc.iotkit.manager.model.vo.SpaceInfo;
import cc.iotkit.model.*;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.space.SpaceDevice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/space")
public class SpaceController {

    @Autowired
    private SpaceDeviceRepository spaceDeviceRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceCache deviceCache;
    @Autowired
    private ProductCache productCache;

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
            DeviceInfo deviceInfo = deviceCache.findByDeviceId(sd.getDeviceId());
            Product product = productCache.findById(deviceInfo.getProductKey());
            deviceVos.add(SpaceDeviceVo.builder()
                    .deviceId(sd.getDeviceId())
                    .name(sd.getName())
                    .picUrl(product.getImg())
                    .spaceName(sd.getSpaceName())
                    .online(deviceInfo.getState().isOnline())
                    .property(deviceInfo.getProperty())
                    .productKey(deviceInfo.getProductKey())
                    .build());
        });
        return deviceVos;
    }

}
