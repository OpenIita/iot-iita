package cc.iotkit.manager.service;

import cc.iotkit.dao.AligenieDeviceRepository;
import cc.iotkit.dao.AligenieProductDao;
import cc.iotkit.dao.DeviceCache;
import cc.iotkit.dao.SpaceDeviceRepository;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.space.SpaceDevice;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.aligenie.AligenieDevice;
import cc.iotkit.model.aligenie.AligenieProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AligenieService {

    @Autowired
    private SpaceDeviceRepository spaceDeviceRepository;

    @Autowired
    private AligenieDeviceRepository aligenieDeviceRepository;

    @Autowired
    private AligenieProductDao aligenieProductDao;

    @Autowired
    private DeviceCache deviceCache;

    public void syncDevice(UserInfo user) {
        if (!user.getUsePlatforms().isAligenie()) {
            //清空
            List<AligenieDevice> aligenieDevices = aligenieDeviceRepository.findAll(Example.of(
                    AligenieDevice.builder()
                            .uid(user.getId())
                            .build()));
            for (AligenieDevice aligenieDevice : aligenieDevices) {
                aligenieDeviceRepository.delete(aligenieDevice);
            }
            return;
        }

        //找出用户空间下所有设备
        List<SpaceDevice> spaceDeviceList = spaceDeviceRepository.findAll(Example.of(
                SpaceDevice.builder().uid(user.getId()).build()
        ));

        for (SpaceDevice spaceDevice : spaceDeviceList) {
            AligenieDevice aligenieDevice = aligenieDeviceRepository.findOne(Example.of(
                    AligenieDevice.builder()
                            .deviceId(spaceDevice.getDeviceId())
                            .uid(user.getId())
                            .build()
            )).orElse(null);

            //不存在设备，新增
            if (aligenieDevice == null) {
                DeviceInfo deviceInfo = deviceCache.findByDeviceId(spaceDevice.getDeviceId());
                AligenieProduct aligenieProduct = aligenieProductDao.getAligenieProduct(deviceInfo.getProductKey());
                if(aligenieProduct==null){
                    continue;
                }

                aligenieDeviceRepository.save(
                        AligenieDevice.builder()
                                .uid(user.getId())
                                .deviceId(spaceDevice.getDeviceId())
                                .name(spaceDevice.getName())
                                .spaceName(spaceDevice.getSpaceName())
                                .productId(aligenieProduct.getProductId())
                                .build()
                );
            } else {
                //存在，更新设备信息
                aligenieDeviceRepository.save(
                        AligenieDevice.builder()
                                .id(aligenieDevice.getId())
                                .name(spaceDevice.getName())
                                .spaceName(spaceDevice.getSpaceName())
                                .build()
                );
            }

        }
    }

}
