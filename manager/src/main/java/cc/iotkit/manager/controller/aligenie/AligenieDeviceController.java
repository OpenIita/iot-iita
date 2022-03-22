package cc.iotkit.manager.controller.aligenie;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.dao.AligenieDeviceRepository;
import cc.iotkit.dao.DeviceRepository;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.aligenie.AligenieDevice;
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
            aligenieDeviceRepository.save(AligenieDevice.builder()
                    .uid(uid)
                    .deviceId(device.getDeviceId())
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
