package cc.iotkit.data.service;

import cc.iotkit.data.manager.IProtocolComponentData;
import cc.iotkit.data.dao.ProtocolComponentRepository;
import cc.iotkit.data.convert.ProtocolComponentMapper;
import cc.iotkit.data.model.TbProtocolComponent;
import cc.iotkit.model.Paging;
import cc.iotkit.model.protocol.ProtocolComponent;
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
public class ProtocolComponentDataImpl implements IProtocolComponentData {

    @Autowired
    private ProtocolComponentRepository protocolComponentRepository;

    @Override
    public List<ProtocolComponent> findByState(String state) {
        return ProtocolComponentMapper.toDto(protocolComponentRepository.findByState(state));
    }

    @Override
    public List<ProtocolComponent> findByStateAndType(String state, String type) {
        return ProtocolComponentMapper.toDto(protocolComponentRepository.findByStateAndType(state, type));
    }

    @Override
    public List<ProtocolComponent> findByUid(String uid) {
        return ProtocolComponentMapper.toDto(protocolComponentRepository.findByUid(uid));
    }

    @Override
    public Paging<ProtocolComponent> findByUid(String uid, int page, int size) {
        Page<TbProtocolComponent> paged = protocolComponentRepository.findByUid(uid,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                ProtocolComponentMapper.toDto(paged.getContent()));
    }

    @Override
    public long countByUid(String uid) {
        return protocolComponentRepository.countByUid(uid);
    }

    @Override
    public ProtocolComponent findById(String s) {
        return ProtocolComponentMapper.M.toDto(protocolComponentRepository.findById(s).orElse(null));
    }

    @Override
    public ProtocolComponent save(ProtocolComponent data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            data.setCreateAt(System.currentTimeMillis());
        }
        protocolComponentRepository.save(ProtocolComponentMapper.M.toVo(data));
        return data;
    }

    @Override
    public ProtocolComponent add(ProtocolComponent data) {
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        protocolComponentRepository.deleteById(s);
    }

    @Override
    public long count() {
        return protocolComponentRepository.count();
    }

    @Override
    public List<ProtocolComponent> findAll() {
        return ProtocolComponentMapper.toDto(protocolComponentRepository.findAll());
    }

    @Override
    public Paging<ProtocolComponent> findAll(int page, int size) {
        Page<TbProtocolComponent> paged = protocolComponentRepository
                .findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                ProtocolComponentMapper.toDto(paged.getContent()));
    }
}
