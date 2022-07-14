package cc.iotkit.data.service;

import cc.iotkit.data.IProtocolComponentData;
import cc.iotkit.data.dao.ProtocolComponentRepository;
import cc.iotkit.data.model.ProtocolComponentMapper;
import cc.iotkit.model.Paging;
import cc.iotkit.model.protocol.ProtocolComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        return new ArrayList<>();
    }

    @Override
    public List<ProtocolComponent> findByUid(String uid) {
        return null;
    }

    @Override
    public Paging<ProtocolComponent> findByUid(String uid, int page, int size) {
        return null;
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }

    @Override
    public ProtocolComponent findById(String s) {
        return null;
    }

    @Override
    public ProtocolComponent save(ProtocolComponent data) {
        return null;
    }

    @Override
    public ProtocolComponent add(ProtocolComponent data) {
        return null;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<ProtocolComponent> findAll() {
        return null;
    }

    @Override
    public Paging<ProtocolComponent> findAll(int page, int size) {
        return null;
    }
}
