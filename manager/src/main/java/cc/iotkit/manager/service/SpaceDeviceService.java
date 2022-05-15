package cc.iotkit.manager.service;

import cc.iotkit.dao.DeviceCache;
import cc.iotkit.dao.ProductCache;
import cc.iotkit.dao.SpaceDeviceRepository;
import cc.iotkit.manager.model.vo.SpaceDeviceVo;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.space.SpaceDevice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpaceDeviceService {
    @Autowired
    private SpaceDeviceRepository spaceDeviceRepository;
    @Autowired
    private DeviceCache deviceCache;
    @Autowired
    private ProductCache productCache;

    public List<SpaceDeviceVo> getUserDevices(String uid, String spaceId) {
        SpaceDevice device = new SpaceDevice();
        device.setUid(uid);
        if (StringUtils.isNotBlank(spaceId)) {
            device.setSpaceId(spaceId);
        }
        List<SpaceDevice> spaceDevices = spaceDeviceRepository.findAll(Example.of(device));
        List<SpaceDeviceVo> spaceDeviceVos = new ArrayList<>();
        spaceDevices.forEach(sd -> {
            DeviceInfo deviceInfo = deviceCache.get(sd.getDeviceId());
            Product product = productCache.findById(deviceInfo.getProductKey());
            spaceDeviceVos.add(SpaceDeviceVo.builder()
                    .uid(sd.getUid())
                    .deviceId(sd.getDeviceId())
                    .name(sd.getName())
                    .picUrl(product.getImg())
                    .online(deviceInfo.getState().isOnline())
                    .property(deviceInfo.getProperty())
                    .productKey(deviceInfo.getProductKey())
                    .build());
        });
        return spaceDeviceVos;
    }
}
