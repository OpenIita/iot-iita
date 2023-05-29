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
import cc.iotkit.data.manager.IVirtualDeviceData;
import cc.iotkit.data.dao.VirtualDeviceMappingRepository;
import cc.iotkit.data.dao.VirtualDeviceRepository;
import cc.iotkit.data.model.TbVirtualDevice;
import cc.iotkit.data.model.TbVirtualDeviceMapping;
import cc.iotkit.data.service.convert.VirtualDeviceMapper;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.device.VirtualDevice;
import cn.hutool.core.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
public class VirtualDeviceDataImpl implements IVirtualDeviceData {

    @Autowired
    private VirtualDeviceRepository virtualDeviceRepository;

    @Autowired
    private VirtualDeviceMappingRepository virtualDeviceMappingRepository;

    @Override
    public List<VirtualDevice> findByUid(String uid) {
        return VirtualDeviceMapper.toDto(virtualDeviceRepository.findByUid(uid));
    }

    @Override
    public Paging<VirtualDevice> findByUid(String uid, int size, int page) {
        Page<TbVirtualDevice> paged = virtualDeviceRepository.findByUid(uid,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                VirtualDeviceMapper.toDto(paged.getContent()));
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }

    @Override
    public List<VirtualDevice> findByUidAndState(String uid, String state) {
        return null;
    }

    @Override
    public List<VirtualDevice> findByTriggerAndState(String trigger, String state) {
        List<VirtualDevice> list = VirtualDeviceMapper.toDto(virtualDeviceRepository
                .findByTriggerAndState(trigger, state));
        for (VirtualDevice virtualDevice : list) {
            virtualDevice.setDevices(getVirtualDeviceIds(virtualDevice.getId()));
        }
        return list;
    }

    @Override
    public VirtualDevice findById(String s) {
        VirtualDevice dto = VirtualDeviceMapper.M.toDto(virtualDeviceRepository.findById(s).orElse(null));
        dto.setDevices(getVirtualDeviceIds(s));
        return dto;
    }

    @Override
    public List<VirtualDevice> findByIds(Collection<String> id) {
        return null;
    }

    private List<String> getVirtualDeviceIds(String virtualId) {
        List<TbVirtualDeviceMapping> deviceMappings = virtualDeviceMappingRepository.findByVirtualId(virtualId);
        return deviceMappings.stream().map(TbVirtualDeviceMapping::getDeviceId).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VirtualDevice save(VirtualDevice data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(IdUtil.simpleUUID());
            data.setCreateAt(System.currentTimeMillis());
        }
        virtualDeviceRepository.save(VirtualDeviceMapper.M.toVo(data));

        //删除旧的添加新的关联设备记录
        virtualDeviceMappingRepository.deleteByVirtualId(data.getId());
        virtualDeviceMappingRepository.saveAllAndFlush(
                data.getDevices().stream().map(d -> new TbVirtualDeviceMapping(
                        IdUtil.simpleUUID(),
                        data.getId(),
                        d
                )).collect(Collectors.toList())
        );
        return data;
    }

    @Override
    public void batchSave(List<VirtualDevice> data) {

    }

    @Override
    @Transactional
    public void deleteById(String s) {
        virtualDeviceRepository.deleteById(s);
        virtualDeviceMappingRepository.deleteByVirtualId(s);
    }

    @Override
    public void deleteByIds(Collection<String> strings) {

    }


    @Override
    public long count() {
        return virtualDeviceRepository.count();
    }

    @Override
    public List<VirtualDevice> findAll() {
        return VirtualDeviceMapper.toDto(virtualDeviceRepository.findAll());
    }

    @Override
    public Paging<VirtualDevice> findAll(PageRequest<VirtualDevice> pageRequest) {
        return null;
    }

    @Override
    public List<VirtualDevice> findAllByCondition(VirtualDevice data) {
        return null;
    }

    @Override
    public VirtualDevice findOneByCondition(VirtualDevice data) {
        return null;
    }


}
