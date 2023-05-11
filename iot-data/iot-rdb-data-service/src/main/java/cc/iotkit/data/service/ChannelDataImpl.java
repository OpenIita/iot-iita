package cc.iotkit.data.service;

import cc.iotkit.data.IChannelData;
import cc.iotkit.data.dao.ChannelRepository;
import cc.iotkit.data.model.ChannelMapper;
import cc.iotkit.data.model.TbChannel;
import cc.iotkit.model.Paging;
import cc.iotkit.model.notify.Channel;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author: 石恒
 * date: 2023-05-11 17:44
 * description:
 **/
@Primary
@Service
public class ChannelDataImpl implements IChannelData {

    @Resource
    private ChannelRepository channelRepository;

    @Override
    public Channel findById(String s) {
        return null;
    }

    @Override
    public Channel save(Channel data) {
        return null;
    }

    @Override
    public Channel add(Channel data) {
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
    public List<Channel> findAll() {
        return null;
    }

    @Override
    public Paging<Channel> findAll(int page, int size) {
        Page<TbChannel> tbDeviceConfigs = channelRepository.findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(
                tbDeviceConfigs.getTotalElements(),
                tbDeviceConfigs.getContent()
                        .stream().map(ChannelMapper.M::toDto)
                        .collect(Collectors.toList())
        );
    }
}
