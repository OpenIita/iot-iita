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

import cc.iotkit.data.IProtocolConverterData;
import cc.iotkit.data.dao.ProtocolConverterRepository;
import cc.iotkit.data.model.ProtocolConverterMapper;
import cc.iotkit.data.model.TbProtocolConverter;
import cc.iotkit.model.Paging;
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
        return ProtocolConverterMapper.toDto(protocolConverterRepository.findByUid(uid));
    }

    @Override
    public Paging<ProtocolConverter> findByUid(String uid, int page, int size) {
        Page<TbProtocolConverter> paged = protocolConverterRepository
                .findByUid(uid, Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                ProtocolConverterMapper.toDto(paged.getContent()));
    }

    @Override
    public long countByUid(String uid) {
        return protocolConverterRepository.countByUid(uid);
    }

    @Override
    public ProtocolConverter findById(String s) {
        return ProtocolConverterMapper.M.toDto(
                protocolConverterRepository.findById(s).orElse(null));
    }

    @Override
    public ProtocolConverter save(ProtocolConverter data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            data.setCreateAt(System.currentTimeMillis());
        }
        protocolConverterRepository.save(ProtocolConverterMapper.M.toVo(data));
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
    public long count() {
        return protocolConverterRepository.count();
    }

    @Override
    public List<ProtocolConverter> findAll() {
        return ProtocolConverterMapper.toDto(protocolConverterRepository.findAll());
    }

    @Override
    public Paging<ProtocolConverter> findAll(int page, int size) {
        Page<TbProtocolConverter> paged = protocolConverterRepository
                .findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                ProtocolConverterMapper.toDto(paged.getContent()));
    }
}
