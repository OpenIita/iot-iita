package cc.iotkit.manager.controller.api;

import cc.iotkit.common.exception.OfflineException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.AppDesignRepository;
import cc.iotkit.dao.DeviceRepository;
import cc.iotkit.dao.SpaceDeviceRepository;
import cc.iotkit.dao.UserActionLogRepository;
import cc.iotkit.manager.model.vo.AppPageNode;
import cc.iotkit.manager.service.AppDesignService;
import cc.iotkit.manager.service.DeviceService;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.InvokeResult;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.space.SpaceDevice;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    private UserActionLogRepository userActionLogRepository;
    @Autowired
    private AppDesignService appDesignService;

    @ApiOperation("设备列表")
    @GetMapping("/list")
    public List<DeviceInfo> list() {
        return deviceRepository.findAll();
    }

    @ApiOperation("设备详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "设备ID", name = "deviceId", required = true, dataType = "String"),
    })
    @GetMapping("/{deviceId}")
    public DeviceInfo detail(@PathVariable("deviceId") String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            throw new RuntimeException("deviceId is blank.");
        }
        return deviceRepository.findById(deviceId).orElseThrow(() -> new RuntimeException("device not found."));
    }

    @ApiOperation("获取用户所有设备ID列表")
    @GetMapping("/getAllDeviceIds")
    public List<String> getAllDeviceIds() {
        List<SpaceDevice> spaceDevices = spaceDeviceRepository.findAll(
                Example.of(SpaceDevice.builder().uid(AuthUtil.getUserId()).build()));
        return spaceDevices.stream()
                .map(SpaceDevice::getDeviceId)
                .collect(Collectors.toList());
    }

    @ApiOperation("设备服务调用")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "设备ID", name = "deviceId", required = true, dataType = "String"),
            @ApiImplicitParam(value = "服务名", name = "service", required = true, dataType = "String"),
            @ApiImplicitParam(value = "参数", name = "args", required = true, dataType = "String"),
    })
    @PostMapping("/{deviceId}/service/{service}")
    public InvokeResult invokeService(@PathVariable("deviceId") String deviceId,
                                      @PathVariable("service") String service,
                                      String args) {
        InvokeResult result = new InvokeResult("", InvokeResult.FAILED_UNKNOWN);
        SpaceDevice device = checkOwner(deviceId);

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
            if ("property/set".equals(service)) {
                requestId = deviceService.setProperty(deviceId,
                        JsonUtil.parse(args, Map.class));
            } else {
                requestId = deviceService.invokeService(deviceId, service,
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

    @ApiOperation("设备属性调用")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "设备ID", name = "deviceId", required = true, dataType = "String"),
            @ApiImplicitParam(value = "参数", name = "args", required = true, dataType = "String"),
    })
    @PostMapping("/{deviceId}/service/property/set")
    public InvokeResult setProperty(@PathVariable("deviceId") String deviceId,
                                    String args) {
        checkOwner(deviceId);
        if (StringUtils.isBlank(deviceId) || StringUtils.isBlank(args)) {
            throw new RuntimeException("deviceId/args is blank.");
        }
        return invokeService(deviceId, "property/set", args);
    }

    /**
     * 检查设备是否属于该用户
     */
    private SpaceDevice checkOwner(String deviceId) {
        return spaceDeviceRepository.findOne(Example.of(SpaceDevice.builder()
                .uid(AuthUtil.getUserId()).deviceId(deviceId).build()))
                .orElse(null);
    }

    @GetMapping("/detailPage/{deviceId}")
    public List<AppPageNode> deviceDetailPage(@PathVariable("deviceId") String deviceId) {
        DeviceInfo device = deviceRepository.findById(deviceId).orElseThrow(() -> new RuntimeException("device not found"));
        return appDesignService.getAppPageNodes(device.getProductKey());
    }

}
