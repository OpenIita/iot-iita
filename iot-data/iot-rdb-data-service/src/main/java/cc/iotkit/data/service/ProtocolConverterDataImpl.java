package cc.iotkit.data.service;

import cc.iotkit.data.IProtocolConverterData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.protocol.ProtocolConverter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProtocolConverterDataImpl implements IProtocolConverterData {
    @Override
    public List<ProtocolConverter> findByUid(String uid) {
        return null;
    }

    @Override
    public Paging<ProtocolConverter> findByUid(String uid, int page, int size) {
        return null;
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }

    @Override
    public ProtocolConverter findById(String s) {
        return null;
    }

    @Override
    public ProtocolConverter save(ProtocolConverter data) {
        return null;
    }

    @Override
    public ProtocolConverter add(ProtocolConverter data) {
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
    public List<ProtocolConverter> findAll() {
        return null;
    }

    @Override
    public Paging<ProtocolConverter> findAll(int page, int size) {
        return null;
    }
}
