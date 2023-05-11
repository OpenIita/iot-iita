package cc.iotkit.data.service;

import cc.iotkit.data.IChannelTemplateData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.notify.ChannelTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author: 石恒
 * date: 2023-05-11 17:45
 * description:
 **/
@Primary
@Service
public class ChannelTemplateDataImpl implements IChannelTemplateData {
    @Override
    public ChannelTemplate findById(String s) {
        return null;
    }

    @Override
    public ChannelTemplate save(ChannelTemplate data) {
        return null;
    }

    @Override
    public ChannelTemplate add(ChannelTemplate data) {
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
    public List<ChannelTemplate> findAll() {
        return null;
    }

    @Override
    public Paging<ChannelTemplate> findAll(int page, int size) {
        return null;
    }
}
