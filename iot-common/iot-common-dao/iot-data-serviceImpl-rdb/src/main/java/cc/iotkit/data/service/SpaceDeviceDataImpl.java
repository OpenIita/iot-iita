/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.ISpaceDeviceData;
import cc.iotkit.data.dao.SpaceDeviceRepository;
import cc.iotkit.data.model.TbSpaceDevice;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.space.SpaceDevice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Primary
@Service
public class SpaceDeviceDataImpl implements ISpaceDeviceData, IJPACommData<SpaceDevice, String> {

    @Autowired
    private SpaceDeviceRepository spaceDeviceRepository;


    @Override
    public JpaRepository getBaseRepository() {
        return spaceDeviceRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSpaceDevice.class;
    }

    @Override
    public Class getTClass() {
        return SpaceDevice.class;
    }

    @Override
    public List<SpaceDevice> findByUidOrderByUseAtDesc(String uid) {
        return MapstructUtils.convert(spaceDeviceRepository.findByUidOrderByUseAtDesc(uid), SpaceDevice.class);
    }

    @Override
    public List<SpaceDevice> findByHomeIdAndCollect(String homeId, boolean collect) {
        return MapstructUtils.convert(spaceDeviceRepository.findByHomeIdAndCollect(homeId, collect), SpaceDevice.class);
    }

    @Override
    public List<SpaceDevice> findByUidOrderByAddAtDesc(String uid) {
        return MapstructUtils.convert(spaceDeviceRepository.findByUidOrderByAddAtDesc(uid), SpaceDevice.class);
    }

    @Override
    public List<SpaceDevice> findBySpaceIdOrderByAddAtDesc(String spaceId) {
        return MapstructUtils.convert(spaceDeviceRepository.findBySpaceIdOrderByAddAtDesc(spaceId), SpaceDevice.class);
    }

    @Override
    public List<SpaceDevice> findByUidAndSpaceIdOrderByAddAtDesc(String uid, String spaceId) {
        return MapstructUtils.convert(spaceDeviceRepository.findByUidAndSpaceIdOrderByAddAtDesc(uid, spaceId), SpaceDevice.class);
    }

    @Override
    public SpaceDevice findByDeviceId(String deviceId) {
        return MapstructUtils.convert(spaceDeviceRepository.findByDeviceId(deviceId), SpaceDevice.class);
    }

    @Override
    public SpaceDevice findByDeviceIdAndUid(String deviceId, String uid) {
        return MapstructUtils.convert(spaceDeviceRepository.findByDeviceIdAndUid(deviceId, uid), SpaceDevice.class);
    }

    @Override
    public List<SpaceDevice> findByUid(String uid) {
        return MapstructUtils.convert(spaceDeviceRepository.findByUid(uid), SpaceDevice.class);
    }

    @Override
    public Paging<SpaceDevice> findByUid(String uid, int page, int size) {
        return new Paging<>();
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }



    @Override
    public SpaceDevice findById(String s) {
        return MapstructUtils.convert(spaceDeviceRepository.findById(s).orElse(null), SpaceDevice.class);
    }



    @Override
    public SpaceDevice save(SpaceDevice data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            data.setAddAt(System.currentTimeMillis());
        }
        spaceDeviceRepository.save(MapstructUtils.convert(data, TbSpaceDevice.class));
        return data;
    }


    @Override
    public List<SpaceDevice> findAll() {
        return MapstructUtils.convert(spaceDeviceRepository.findAll(), SpaceDevice.class);
    }




}
