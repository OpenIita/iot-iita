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

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.manager.IProtocolConverterData;
import cc.iotkit.data.dao.ProtocolConverterRepository;
import cc.iotkit.data.model.TbProtocolConverter;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.protocol.ProtocolConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Primary
@Service
public class ProtocolConverterDataImpl implements IProtocolConverterData {

    @Autowired
    private ProtocolConverterRepository protocolConverterRepository;

    @Override
    public List<ProtocolConverter> findByUid(String uid) {
        return MapstructUtils.convert(protocolConverterRepository.findByUid(uid), ProtocolConverter.class);
    }

    @Override
    public Paging<ProtocolConverter> findByUid(String uid, int page, int size) {
        Page<TbProtocolConverter> paged = protocolConverterRepository
                .findByUid(uid, Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                MapstructUtils.convert(paged.getContent(), ProtocolConverter.class));
    }

    @Override
    public long countByUid(String uid) {
        return protocolConverterRepository.countByUid(uid);
    }

    @Override
    public ProtocolConverter findById(String s) {
        return MapstructUtils.convert(
                protocolConverterRepository.findById(s).orElse(null), ProtocolConverter.class);
    }

    @Override
    public ProtocolConverter save(ProtocolConverter data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            data.setCreateAt(System.currentTimeMillis());
        }
        protocolConverterRepository.save(MapstructUtils.convert(data, TbProtocolConverter.class));
        return data;
    }

    @Override
    public ProtocolConverter add(ProtocolConverter data) {
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        protocolConverterRepository.deleteById(s);
    }

    @Override
    public void deleteByIds(String[] strings) {

    }

    @Override
    public long count() {
        return protocolConverterRepository.count();
    }

    @Override
    public List<ProtocolConverter> findAll() {
        return MapstructUtils.convert(protocolConverterRepository.findAll(), ProtocolConverter.class);
    }

    @Override
    public Paging<ProtocolConverter> findAll(int page, int size) {
        Page<TbProtocolConverter> paged = protocolConverterRepository
                .findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                MapstructUtils.convert(paged.getContent(), ProtocolConverter.class));
    }
}
