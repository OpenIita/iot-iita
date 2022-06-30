/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller.aligenie;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.dao.*;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.service.DeviceService;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.aligenie.AligenieDevice;
import cc.iotkit.model.aligenie.AligenieProduct;
import cc.iotkit.model.device.DeviceInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/aligenieDevice")
public class AligenieDeviceController {

    @Autowired
    private AligenieDeviceRepository aligenieDeviceRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private DataOwnerService ownerService;
    @Autowired
    private DeviceInfoRepository deviceInfoRepository;
    @Autowired
    private AligenieProductRepository aligenieProductRepository;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceDao deviceDao;

    @GetMapping("/list/{uid}")
    public List<AligenieDevice> getDevices(@PathVariable("uid") String uid) {
        UserInfo user = userInfoRepository.findById(uid).get();
        ownerService.checkOwner(user);
        return aligenieDeviceRepository.findByUid(uid);
    }

    @PostMapping("/bind/{uid}")
    public void bind(@PathVariable("uid") String uid,
                     @RequestBody List<Device> devices) {
        Optional<UserInfo> optUser = userInfoRepository.findById(uid);
        if (optUser.isEmpty()) {
            throw new BizException("user does not exist");
        }
        UserInfo user = optUser.get();
        ownerService.checkOwner(user);
        String token = "";
        List<AligenieDevice> aligenieDevices = aligenieDeviceRepository.findByUid(uid);
        //先清除待删除的标签
        for (AligenieDevice alDevice : aligenieDevices) {
            deviceDao.setTagNull(alDevice.getDeviceId(), "aligenie");
            token = alDevice.getToken();
        }
        //删除原有的设备
        aligenieDeviceRepository.deleteByUid(uid);

        for (Device device : devices) {
            DeviceInfo deviceInfo = deviceInfoRepository.findById(device.getDeviceId()).get();
            AligenieProduct product = aligenieProductRepository.findByProductKey(deviceInfo.getProductKey());
            aligenieDeviceRepository.save(AligenieDevice.builder()
                    .uid(user.getId())
                    .token(token)
                    .deviceId(device.getDeviceId())
                    .productId(product.getProductId())
                    .spaceName("客厅")
                    .name(device.getName())
                    .build());

            //设置天猫精灵接入标签
            deviceDao.updateTag(device.getDeviceId(),
                    new DeviceInfo.Tag("aligenie", "天猫精灵接入", "是"));
        }
    }

    @Data
    public static class Device {
        private String deviceId;
        private String name;
    }

}
