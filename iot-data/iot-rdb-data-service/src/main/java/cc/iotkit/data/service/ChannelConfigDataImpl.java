package cc.iotkit.data.service;

import cc.iotkit.data.IChannelConfigData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.notify.ChannelConfig;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author: 石恒
 * date: 2023-05-11 17:43
 * description:
 **/
@Primary
@Service
public class ChannelConfigDataImpl implements IChannelConfigData {

    @Override
    public ChannelConfig findById(String s) {
        return null;
    }

    @Override
    public ChannelConfig save(ChannelConfig data) {
        return null;
    }

    @Override
    public ChannelConfig add(ChannelConfig data) {
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
    public List<ChannelConfig> findAll() {
        return null;
    }

    @Override
    public Paging<ChannelConfig> findAll(int page, int size) {
        return null;
    }
}
