/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller;

import cc.iotkit.common.Constants;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.dao.*;
import cc.iotkit.manager.model.vo.FindDeviceVo;
import cc.iotkit.manager.model.vo.SpaceDeviceVo;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.utils.AuthUtil;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.Category;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.space.Space;
import cc.iotkit.model.space.SpaceDevice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/space")
public class SpaceDeviceController {

    @Autowired
    private SpaceDeviceRepository spaceDeviceRepository;
    @Autowired
    private DeviceInfoRepository deviceInfoRepository;
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
    @Autowired
    private UserInfoRepository userInfoRepository;

    /**
     * 我最近使用的设备列表
     */
    @GetMapping(Constants.API_SPACE.RECENT_DEVICES)
    public List<SpaceDeviceVo> getMyRecentDevices() {
        List<SpaceDevice> spaceDevices = spaceDeviceRepository.findByUidOrderByUseAtDesc(AuthUtil.getUserId());
        return spaceDevices.stream().map((this::parseSpaceDevice)).collect(Collectors.toList());
    }

    /**
     * 我的空间设备列表-按空间获取
     *
     * @param spaceId 空间id
     */
    @GetMapping(Constants.API_SPACE.SPACE_DEVICES)
    public List<SpaceDeviceVo> getMyDevices(@PathVariable("spaceId") String spaceId) {
        String uid = AuthUtil.getUserId();
        List<SpaceDevice> spaceDevices;
        if ("all".equals(spaceId)) {
            //全部设备
            spaceDevices = spaceDeviceRepository.findByUidOrderByUseAtDesc(uid);
        } else {
            //按空间获取
            spaceDevices = spaceDeviceRepository.
                    findByUidAndSpaceIdOrderByAddAtDesc(uid, spaceId);
        }
        return spaceDevices.stream().map((this::parseSpaceDevice)).collect(Collectors.toList());
    }

    private SpaceDeviceVo parseSpaceDevice(SpaceDevice sd) {
        DeviceInfo device = deviceInfoRepository.findByDeviceId(sd.getDeviceId());
        Space space = spaceCache.getSpace(sd.getSpaceId());
        Product product = productCache.findById(device.getProductKey());
        Category category = categoryCache.getById(product.getCategory());
        DeviceInfo.State state = device.getState();

        return SpaceDeviceVo.builder()
                .id(sd.getId())
                .deviceId(sd.getDeviceId())
                .deviceName(device.getDeviceName())
                .name(sd.getName())
                .spaceId(sd.getSpaceId())
                .spaceName(space.getName())
                .productKey(device.getProductKey())
                .productName(product.getName())
                .category(product.getCategory())
                .categoryName(category.getName())
                .picUrl(product.getImg())
                .online(state != null && state.isOnline())
                .property(device.getProperty())
                .uid(sd.getUid())
                .build();
    }

    /**
     * 获取用户所有设备列表
     */
    @GetMapping("/{userId}/devices")
    public List<SpaceDeviceVo> getDevices(@PathVariable("userId") String userId) {
        List<SpaceDevice> spaceDevices = spaceDeviceRepository.findByUid(userId);
        return spaceDevices.stream().map((this::parseSpaceDevice)).collect(Collectors.toList());
    }

    /**
     * 搜索未添加过的设备
     */
    @GetMapping(Constants.API_SPACE.FIND_DEVICE)
    List<FindDeviceVo> findDevice(String mac) {
        if (StringUtils.isBlank(mac)) {
            throw new BizException("mac is blank");
        }

        List<FindDeviceVo> findDeviceVos = new ArrayList<>();
        List<DeviceInfo> devices = deviceInfoRepository.findByDeviceName(mac);
        if (devices == null) {
            return findDeviceVos;
        }

        //查找网关下子设备
        List<DeviceInfo> subDevices = new ArrayList<>();
        for (DeviceInfo device : devices) {
            if (device.getParentId() == null) {
                subDevices = deviceInfoRepository.findByParentId(device.getDeviceId());
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

    /**
     * 往指定房间中添加设备
     */
    @PostMapping(Constants.API_SPACE.ADD_DEVICE)
    public void addDevice(SpaceDevice device) {
        String deviceId = device.getDeviceId();
        DeviceInfo deviceInfo = deviceInfoRepository.findByDeviceId(deviceId);
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
            deviceInfo.setSubUid(subUid);
        }

        String uid = AuthUtil.getUserId();
        Optional<UserInfo> optUser = userInfoRepository.findById(uid);
        if (!optUser.isPresent()) {
            throw new BizException("user does not exist");
        }
        if (!subUid.contains(uid)) {
            subUid.add(uid);
        }

        //更新设备标签，标识设备是用的哪个第三方平台
        List<String> platforms = optUser.get().getUsePlatforms();
        Map<String, DeviceInfo.Tag> tags = deviceInfo.getTag();
        for (String platform : platforms) {
            Constants.ThirdPlatform thirdPlatform = Constants.ThirdPlatform.valueOf(platform);
            tags.put(platform, new DeviceInfo.Tag(platform, thirdPlatform.desc, "是"));
        }

        deviceInfoRepository.save(deviceInfo);
    }

    /**
     * 移除房间中的设备
     */
    @DeleteMapping(Constants.API_SPACE.REMOVE_DEVICE)
    public void removeDevice(String deviceId) {
        String uid = AuthUtil.getUserId();
        SpaceDevice spaceDevice = spaceDeviceRepository.findByDeviceIdAndUid(deviceId, uid);
        if (spaceDevice == null) {
            throw new BizException("space device does not exist");
        }
        dataOwnerService.checkOwner(spaceDevice);

        spaceDeviceRepository.deleteById(spaceDevice.getId());
        DeviceInfo deviceInfo = deviceInfoRepository.findByDeviceId(deviceId);
        Optional<UserInfo> optUser = userInfoRepository.findById(uid);
        if (optUser.isEmpty()) {
            throw new BizException("user does not exist");
        }

        List<String> platforms = optUser.get().getUsePlatforms();
        List<String> subUid = deviceInfo.getSubUid();
        subUid.remove(uid);
        //删除设备标签
        for (String platform : platforms) {
            deviceInfo.getTag().remove(platform);
        }

        deviceInfoRepository.save(deviceInfo);
    }

    /**
     * 保存房间设备信息
     */
    @PostMapping(Constants.API_SPACE.SAVE_DEVICE)
    public void saveDevice(SpaceDevice spaceDevice) {
        dataOwnerService.checkOwner(spaceDevice);
        Optional<SpaceDevice> optData = spaceDeviceRepository.findById(spaceDevice.getId());
        if (optData.isEmpty()) {
            throw new BizException("space device does not exist");
        }
        SpaceDevice oldData = optData.get();
        oldData.setName(spaceDevice.getName());
        oldData.setSpaceId(spaceDevice.getSpaceId());
        spaceDeviceRepository.save(oldData);
    }

    /**
     * 获取房间中指定设备信息
     */
    @GetMapping(Constants.API_SPACE.GET_DEVICE)
    public SpaceDeviceVo getSpaceDevice(@PathVariable("deviceId") String deviceId) {
        String uid = AuthUtil.getUserId();
        SpaceDevice spaceDevice = spaceDeviceRepository.findByDeviceIdAndUid(deviceId, uid);
        //更新设备使用时间
        spaceDevice.setUseAt(System.currentTimeMillis());
        spaceDeviceRepository.save(spaceDevice);
        return parseSpaceDevice(spaceDevice);
    }

    /**
     * 设置设备的第三方平台openUid
     * 如：小度接入使用的openUid
     */
    @PostMapping(Constants.API_SPACE.SET_OPEN_UID)
    public void setOpenUid(String deviceId, String platform, String openUid) {
        SpaceDevice spaceDevice = spaceDeviceRepository.findByDeviceId(deviceId);
        if (spaceDevice == null) {
            throw new BizException("space device does not exist");
        }

        //只能修改自己的设备
        dataOwnerService.checkOwner(spaceDevice);

        //找到设备
        DeviceInfo deviceInfo = deviceInfoRepository.findByDeviceId(deviceId);
        Map<String, DeviceInfo.Tag> tags = deviceInfo.getTag();
        String openUidName = platform + "OpenUid";
        //给设备添加对应平台openUid的设备标签
        Constants.ThirdOpenUid thirdOpenUid = Constants.ThirdOpenUid.valueOf(openUidName);
        tags.put(openUidName, new DeviceInfo.Tag(openUidName, thirdOpenUid.desc, openUid));
        deviceInfoRepository.save(deviceInfo);
    }
}
