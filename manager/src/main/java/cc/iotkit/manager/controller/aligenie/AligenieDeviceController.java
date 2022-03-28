package cc.iotkit.manager.controller.aligenie;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.exception.OfflineException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.AligenieDeviceRepository;
import cc.iotkit.dao.AligenieProductRepository;
import cc.iotkit.dao.DeviceRepository;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.deviceapi.IDeviceManager;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.InvokeResult;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.aligenie.AligenieDevice;
import cc.iotkit.model.aligenie.AligenieProduct;
import cc.iotkit.model.device.DeviceInfo;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    private DeviceRepository deviceRepository;
    @Autowired
    private AligenieProductRepository aligenieProductRepository;
    @Autowired
    private IDeviceManager deviceManager;

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
                    .uid(user.getId())
                    .deviceId(device.getDeviceId())
                    .productId(product.getProductId())
                    .spaceName("客厅")
                    .name(device.getName())
                    .build());
        }

    }

    @ApiOperation("设备服务调用")
    @PostMapping("/invoke/{deviceId}/{service}")
    public InvokeResult invokeService(@PathVariable("deviceId") String deviceId,
                                      @PathVariable("service") String service,
                                      String args) {
        InvokeResult result = new InvokeResult("", InvokeResult.FAILED_UNKNOWN);
        AligenieDevice device = aligenieDeviceRepository.findByUidAndDeviceId(AuthUtil.getUserId(), deviceId);

        if (device == null) {
            result.setCode(InvokeResult.FAILED_NO_AUTH);
            return result;
        }

        if (StringUtils.isBlank(deviceId) || StringUtils.isBlank(service)) {
            log.error("deviceId/service is blank");
            result.setCode(InvokeResult.FAILED_PARAM_ERROR);
            return result;
        }

        try {
            String requestId;
            if ("set".equals(service)) {
                requestId = deviceManager.setProperty(deviceId,
                        JsonUtil.parse(args, Map.class));
            } else {
                requestId = deviceManager.invokeService(deviceId, service,
                        JsonUtil.parse(args, Map.class));
            }
            result.setRequestId(requestId);
            result.setCode(InvokeResult.SUCCESS);
        } catch (OfflineException e) {
            log.error("sendMsg failed", e);
            result.setCode(InvokeResult.FAILED_OFFLINE);
            return result;
        }
        return result;
    }

    @Data
    public static class Device {
        private String deviceId;
        private String name;
    }

}
