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

import cc.iotkit.data.ISpaceDeviceData;
import cc.iotkit.data.dao.SpaceDeviceRepository;
import cc.iotkit.data.convert.SpaceDeviceMapper;
import cc.iotkit.model.Paging;
import cc.iotkit.model.space.SpaceDevice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Primary
@Service
public class SpaceDeviceDataImpl implements ISpaceDeviceData {

    @Autowired
    private SpaceDeviceRepository spaceDeviceRepository;

    @Override
    public List<SpaceDevice> findByUidOrderByUseAtDesc(String uid) {
        return SpaceDeviceMapper.toDto(spaceDeviceRepository.findByUidOrderByUseAtDesc(uid));
    }

    @Override
    public List<SpaceDevice> findByHomeIdAndCollect(String homeId,boolean collect) {
        return SpaceDeviceMapper.toDto(spaceDeviceRepository.findByHomeIdAndCollect(homeId,collect));
    }

    @Override
    public List<SpaceDevice> findByUidOrderByAddAtDesc(String uid) {
        return SpaceDeviceMapper.toDto(spaceDeviceRepository.findByUidOrderByAddAtDesc(uid));
    }

    @Override
    public List<SpaceDevice> findBySpaceIdOrderByAddAtDesc(String spaceId) {
        return SpaceDeviceMapper.toDto(spaceDeviceRepository.findBySpaceIdOrderByAddAtDesc(spaceId));
    }

    @Override
    public List<SpaceDevice> findByUidAndSpaceIdOrderByAddAtDesc(String uid, String spaceId) {
        return SpaceDeviceMapper.toDto(spaceDeviceRepository.findByUidAndSpaceIdOrderByAddAtDesc(uid, spaceId));
    }

    @Override
    public SpaceDevice findByDeviceId(String deviceId) {
        return SpaceDeviceMapper.M.toDto(spaceDeviceRepository.findByDeviceId(deviceId));
    }

    @Override
    public SpaceDevice findByDeviceIdAndUid(String deviceId, String uid) {
        return SpaceDeviceMapper.M.toDto(spaceDeviceRepository.findByDeviceIdAndUid(deviceId, uid));
    }

    @Override
    public List<SpaceDevice> findByUid(String uid) {
        return SpaceDeviceMapper.toDto(spaceDeviceRepository.findByUid(uid));
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
        return SpaceDeviceMapper.M.toDto(spaceDeviceRepository.findById(s).orElse(null));
    }

    @Override
    public SpaceDevice save(SpaceDevice data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            data.setAddAt(System.currentTimeMillis());
        }
        spaceDeviceRepository.save(SpaceDeviceMapper.M.toVo(data));
        return data;
    }

    @Override
    public SpaceDevice add(SpaceDevice data) {
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        spaceDeviceRepository.deleteById(s);
    }

    @Override
    public long count() {
        return spaceDeviceRepository.count();
    }

    @Override
    public List<SpaceDevice> findAll() {
        return SpaceDeviceMapper.toDto(spaceDeviceRepository.findAll());
    }

    @Override
    public Paging<SpaceDevice> findAll(int page, int size) {
        return new Paging<>();
    }
}
