package cc.iotkit.manager.controller.api;

import cc.iotkit.dao.AppDesignRepository;
import cc.iotkit.dao.DeviceRepository;
import cc.iotkit.dao.SpaceDeviceRepository;
import cc.iotkit.manager.model.vo.AppPageNode;
import cc.iotkit.manager.service.AppDesignService;
import cc.iotkit.manager.service.DeviceService;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.space.SpaceDevice;
import cc.iotkit.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController("api-device")
@RequestMapping("/api/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private SpaceDeviceRepository spaceDeviceRepository;
    @Autowired
    private AppDesignRepository appDesignRepository;
    @Autowired
    private AppDesignService appDesignService;

    @GetMapping("/list")
    public List<DeviceInfo> list() {
        return deviceRepository.findAll();
    }

    @GetMapping("/{deviceId}")
    public DeviceInfo detail(@PathVariable("deviceId") String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            throw new RuntimeException("deviceId is blank.");
        }
        return deviceRepository.findById(deviceId).orElseThrow(() -> new RuntimeException("device not found."));
    }

    @GetMapping("/getAllDeviceIds")
    public List<String> getAllDeviceIds() {
        List<SpaceDevice> spaceDevices = spaceDeviceRepository.findAll(
                Example.of(SpaceDevice.builder().uid(AuthUtil.getUserId()).build()));
        return spaceDevices.stream()
                .map(SpaceDevice::getDeviceId)
                .collect(Collectors.toList());
    }

    @GetMapping("/detailPage/{deviceId}")
    public List<AppPageNode> deviceDetailPage(@PathVariable("deviceId") String deviceId) {
        DeviceInfo device = deviceRepository.findById(deviceId).orElseThrow(() -> new RuntimeException("device not found"));
        return appDesignService.getAppPageNodes(device.getProductKey());
    }

}
