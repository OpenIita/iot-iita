package cc.iotkit.manager.controller;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.dao.*;
import cc.iotkit.manager.model.vo.FindDeviceVo;
import cc.iotkit.manager.model.vo.SpaceDeviceVo;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.Category;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.space.Space;
import cc.iotkit.model.space.SpaceDevice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private CategoryCache categoryCache;
    @Autowired
    private SpaceCache spaceCache;
    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private DataOwnerService dataOwnerService;

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
    public List<SpaceDeviceVo> getMyDevices(@PathVariable("spaceId") String spaceId) {
        List<SpaceDevice> spaceDevices = spaceDeviceRepository.
                findByUidAndSpaceIdOrderByAddAtDesc(AuthUtil.getUserId(), spaceId);
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
                .category(product.getCategory())
                .picUrl(product.getImg())
                .online(state != null && state.isOnline())
                .property(device.getProperty())
                .uid(sd.getUid())
                .build();
    }

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

    @GetMapping("/findDevice")
    List<FindDeviceVo> findDevice(String mac) {
        if (StringUtils.isBlank(mac)) {
            throw new BizException("mac is blank");
        }

        List<FindDeviceVo> findDeviceVos = new ArrayList<>();
        List<DeviceInfo> devices = deviceRepository.findByDeviceName(mac);
        if (devices == null) {
            return findDeviceVos;
        }

        //查找网关下子设备
        List<DeviceInfo> subDevices = new ArrayList<>();
        for (DeviceInfo device : devices) {
            if (device.getParentId() == null) {
                subDevices = deviceRepository.findByParentId(device.getDeviceId());
            }
        }
        devices.addAll(subDevices);

        //查找空间设备
        for (DeviceInfo device : devices) {
            SpaceDevice spaceDevice = spaceDeviceRepository.findByDeviceId(device.getDeviceId());
            if (spaceDevice == null) {
                //没有被其它人占用
                findDeviceVos.add(getFindDeviceVo(device));
            }
        }
        return findDeviceVos;
    }

    private FindDeviceVo getFindDeviceVo(DeviceInfo device) {
        FindDeviceVo findDeviceVo = FindDeviceVo.builder()
                .deviceId(device.getDeviceId())
                .deviceName(device.getDeviceName())
                .productKey(device.getProductKey())
                .build();

        Product product = productCache.findById(device.getProductKey());
        Category category = categoryCache.getById(product.getCategory());
        findDeviceVo.setProductName(product.getName());
        findDeviceVo.setProductImg(product.getImg());
        findDeviceVo.setCategoryName(category.getName());
        return findDeviceVo;
    }

    @PostMapping("/addDevice")
    public void addDevice(SpaceDevice device) {
        String deviceId = device.getDeviceId();
        DeviceInfo deviceInfo = deviceRepository.findByDeviceId(deviceId);
        if (deviceInfo == null) {
            throw new BizException("device does not exist");
        }
        String spaceId = device.getSpaceId();
        Optional<Space> optSpace = spaceRepository.findById(spaceId);
        if (!optSpace.isPresent()) {
            throw new BizException("space does not exist");
        }

        SpaceDevice oldSpaceDevice = spaceDeviceRepository.findByDeviceId(deviceId);
        if (oldSpaceDevice != null) {
            throw new BizException("device has been added");
        }

        Space space = optSpace.get();

        SpaceDevice spaceDevice = SpaceDevice.builder()
                .deviceId(deviceId)
                .spaceId(spaceId)
                .deviceId(deviceId)
                .name(device.getName())
                .homeId(space.getHomeId())
                .uid(AuthUtil.getUserId())
                .addAt(System.currentTimeMillis())
                .build();
        spaceDeviceRepository.save(spaceDevice);

        //更新设备子用户列表
        List<String> subUid = deviceInfo.getSubUid();
        if (subUid == null) {
            subUid = new ArrayList<>();
        }

        String uid = AuthUtil.getUserId();
        if (!subUid.contains(uid)) {
            subUid.add(uid);
        }
        deviceRepository.save(deviceInfo);
    }

    @DeleteMapping("/removeDevice")
    public void removeDevice(String deviceId) {
        String uid = AuthUtil.getUserId();
        SpaceDevice spaceDevice = spaceDeviceRepository.findByDeviceIdAndUid(deviceId, uid);
        if (spaceDevice == null) {
            throw new BizException("space device does not exist");
        }

        spaceDeviceRepository.deleteById(spaceDevice.getId());
        DeviceInfo deviceInfo = deviceRepository.findByDeviceId(deviceId);
        List<String> subUid = deviceInfo.getSubUid();
        if (subUid != null) {
            subUid.remove(uid);
            deviceRepository.save(deviceInfo);
        }
    }
}
