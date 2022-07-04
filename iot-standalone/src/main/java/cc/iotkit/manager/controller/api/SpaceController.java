/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller.api;

import cc.iotkit.dao.*;
import cc.iotkit.manager.model.vo.SpaceDeviceVo;
import cc.iotkit.manager.service.SpaceDeviceService;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.space.Space;
import cc.iotkit.model.space.SpaceDevice;
import cc.iotkit.utils.AuthUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController("api-space")
@RequestMapping("/api/space")
public class SpaceController {

    @Autowired
    private SpaceDeviceRepository spaceDeviceRepository;
    @Autowired
    private DeviceInfoRepository deviceInfoRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCache productCache;
    @Autowired
    private DeviceCache deviceCache;
    @Autowired
    private SpaceDeviceService spaceDeviceService;
    @Autowired
    private CommonDao commonDao;

    @PostMapping("/addGateway")
    public void addGateway(String pk, String mac, String name, String spaceId) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(mac) || StringUtils.isBlank(spaceId)) {
            throw new RuntimeException("pk/name/mac/spaceId is blank.");
        }
        mac = mac.toUpperCase();
        DeviceInfo device = deviceInfoRepository.findByProductKeyAndDeviceName(pk, mac);
        if (device == null) {
            throw new RuntimeException("未找到该设备");
        }
        Space space = spaceRepository.findById(spaceId).orElseThrow(() -> new RuntimeException("未找到空间"));
        addSpaceDevice(name, device, space);
    }

    @PostMapping("/add")
    public void add(String deviceId, String name, String spaceId) {
        if (StringUtils.isBlank(deviceId) || StringUtils.isBlank(name) || StringUtils.isBlank(spaceId)) {
            throw new RuntimeException("deviceId/name/spaceId is blank.");
        }
        DeviceInfo device = deviceInfoRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("device not found."));
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new RuntimeException("space not found."));
        addSpaceDevice(name, device, space);
    }

    @PostMapping("/scan")
    public List<SpaceDeviceVo> scan() {
        //找到网关产品id
        List<String> gateways = productRepository.findByCategory("gateway")
                .stream().map(Product::getId).collect(Collectors.toList());
        //找到用户已添加的所有设备
        List<SpaceDeviceVo> spaceDeviceVos = spaceDeviceService.getUserDevices(AuthUtil.getUserId(), "");
        //找到已添加的网关
        List<SpaceDeviceVo> userGateways = spaceDeviceVos.stream().filter((sd) ->
                gateways.contains(sd.getProductKey())).collect(Collectors.toList());

        List<DeviceInfo> foundDevices = new ArrayList<>();
        //找到网关下的所有设备
        userGateways.forEach((g) -> foundDevices.addAll(
                deviceInfoRepository.findByParentId(g.getDeviceId())));

        //过滤已添加的设备
        return foundDevices.stream().filter((d) -> {
            boolean exits = false;
            for (SpaceDeviceVo sd : spaceDeviceVos) {
                if (d.getDeviceId().equals(sd.getDeviceId())) {
                    exits = true;
                    break;
                }
            }
            return !exits;
        }).collect(Collectors.toList()).stream().map((d) ->
                //转成空间设备信息
                buildSpaceDeviceVo("", d.getDeviceId(), "", "", ""))
                .collect(Collectors.toList());
    }

    @GetMapping("/devices")
    public List<SpaceDeviceVo> devices(String homeId, String spaceId) {
        Criteria criteria = new Criteria();

        SpaceDevice device = new SpaceDevice();
        device.setUid(AuthUtil.getUserId());
        if (StringUtils.isNotBlank(spaceId)) {
            criteria = criteria.and("spaceId").is(spaceId);
        } else {
            criteria = criteria.and("homeId").is(homeId);
        }
        List<SpaceDevice> spaceDevices = commonDao.find(SpaceDevice.class, criteria);
        List<SpaceDeviceVo> spaceDeviceVos = new ArrayList<>();
        spaceDevices.forEach(sd -> spaceDeviceVos.add(buildSpaceDeviceVo(
                sd.getId(), sd.getDeviceId(),
                sd.getUid(), sd.getName(), "")));
        return spaceDeviceVos;
    }

    @GetMapping("/getSpaceDevice")
    public SpaceDeviceVo getSpaceDevice(String id) {
        SpaceDevice device = spaceDeviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("device not found"));

        return buildSpaceDeviceVo(device.getId(), device.getDeviceId(),
                AuthUtil.getUserId(), device.getName(),
                "");
    }

    @GetMapping("/getDeviceByMac")
    public SpaceDeviceVo getDeviceByMac(String mac) {
        List<DeviceInfo> devices = deviceInfoRepository.findByDeviceName(mac);
        if (devices.size() == 0) {
            throw new RuntimeException("device not found by mac");
        }

        return buildSpaceDeviceVo("", devices.get(0).getDeviceId(),
                AuthUtil.getUserId(), "", "");
    }

    @PostMapping("/delete")
    public void delete(String id) {
        SpaceDevice device = spaceDeviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("空间设备不存在"));
        if (!AuthUtil.getUserId().equals(device.getUid())) {
            throw new RuntimeException("设备不属于该用户");
        }
        spaceDeviceRepository.deleteById(id);
    }

    private SpaceDeviceVo buildSpaceDeviceVo(String id, String deviceId, String uid, String name, String spaceName) {
        DeviceInfo deviceInfo = deviceCache.get(deviceId);
        Product product = productCache.findById(deviceInfo.getProductKey());
        return SpaceDeviceVo.builder()
                .id(id)
                .uid(uid)
                .deviceId(deviceId)
                .name(StringUtils.isBlank(name) ? product.getName() : name)
                .deviceName(deviceInfo.getDeviceName())
                .picUrl(product.getImg())
                .spaceName(spaceName)
                .online(deviceInfo.getState().isOnline())
                .property(deviceInfo.getProperty() == null ? new HashMap<>() : deviceInfo.getProperty())
                .productKey(deviceInfo.getProductKey())
                .build();
    }

    private void addSpaceDevice(String name, DeviceInfo device, Space space) {
        String uid = AuthUtil.getUserId();

        SpaceDevice sd = spaceDeviceRepository.findByDeviceId(device.getDeviceId());
        String id = null;

        //重复添加，更新
        if (sd != null && uid.equals(sd.getUid())) {
            id = sd.getId();
        } else if (sd != null && !uid.equals(sd.getUid())) {
            //已经被别人添加了
            throw new RuntimeException("设备已被其它人添加");
        }

        Product product = productRepository.findById(device.getProductKey()).orElseThrow(() -> new RuntimeException("product not found."));
        spaceDeviceRepository.save(SpaceDevice.builder()
                .id(id)
                .uid(AuthUtil.getUserId())
                .deviceId(device.getDeviceId())
                .name(name == null ? product.getName() : name)
                .homeId(space.getHomeId())
                .spaceId(space.getId())
                .build());
    }

}