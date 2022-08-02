/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.temporal.es.service;

import cc.iotkit.model.Paging;
import cc.iotkit.model.device.VirtualDeviceLog;
import cc.iotkit.temporal.IVirtualDeviceLogData;
import cc.iotkit.temporal.es.dao.VirtualDeviceLogRepository;
import cc.iotkit.temporal.es.document.DocVirtualDeviceLog;
import cc.iotkit.temporal.es.document.VirtualDeviceLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class VirtualDeviceLogDataImpl implements IVirtualDeviceLogData {

    @Autowired
    private VirtualDeviceLogRepository virtualDeviceLogRepository;

    @Override
    public Paging<VirtualDeviceLog> findByVirtualDeviceId(String virtualDeviceId, int page, int size) {
        Page<DocVirtualDeviceLog> paged = virtualDeviceLogRepository
                .findByVirtualDeviceId(virtualDeviceId,
                        Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(), paged.getContent().stream()
                .map(VirtualDeviceLogMapper.M::toDto)
                .collect(Collectors.toList()));
    }

    @Override
    public void add(VirtualDeviceLog log) {
        virtualDeviceLogRepository.save(VirtualDeviceLogMapper.M.toVo(log));
    }
}
