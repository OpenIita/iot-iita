package cc.iotkit.manager.controller.aligenie;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.dao.*;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.aligenie.AligenieDevice;
import cc.iotkit.model.aligenie.AligenieProduct;
import cc.iotkit.model.device.DeviceInfo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    private DeviceRepository deviceRepository;
    @Autowired
    private AligenieProductRepository aligenieProductRepository;

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
        if (!optUser.isPresent()) {
            throw new BizException("user does not exist");
        }
        UserInfo user = optUser.get();
        ownerService.checkOwner(user);

        aligenieDeviceRepository.deleteByUid(uid);
        for (Device device : devices) {
            DeviceInfo deviceInfo = deviceRepository.findById(device.getDeviceId()).get();
            AligenieProduct product = aligenieProductRepository.findByProductKey(deviceInfo.getProductKey());
            aligenieDeviceRepository.save(AligenieDevice.builder()
                    .uid(uid)
                    .deviceId(device.getDeviceId())
                    .productId(product.getProductId())
                    .spaceName("客厅")
                    .name(device.getName())
                    .build());
        }

    }

    @Data
    public static class Device {
        private String deviceId;
        private String name;
    }

}
