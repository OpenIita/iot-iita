package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.manager.IChannelData;
import cc.iotkit.data.dao.ChannelRepository;
import cc.iotkit.data.model.TbChannel;
import cc.iotkit.model.Paging;
import cc.iotkit.model.notify.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

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
    public Channel findById(String id) {
        return MapstructUtils.convert(channelRepository.findById(id).orElse(null), Channel.class);
    }

    @Override
    public Channel save(Channel data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        channelRepository.save(
                MapstructUtils.convert(data, TbChannel.class)
        );
        return data;
    }

    @Override
    public Channel add(Channel data) {
        data.setCreateAt(System.currentTimeMillis());
        return save(data);
    }

    @Override
    public void deleteById(String id) {
        channelRepository.deleteById(id);
    }

    @Override
    public void deleteByIds(String[] strings) {

    }

    @Override
    public long count() {
        return channelRepository.count();
    }

    @Override
    public List<Channel> findAll() {
        return MapstructUtils.convert(channelRepository.findAll(), Channel.class);
    }

    @Override
    public Paging<Channel> findAll(int page, int size) {
        Page<TbChannel> tbDeviceConfigs = channelRepository.findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(
                tbDeviceConfigs.getTotalElements(),
                MapstructUtils.convert(tbDeviceConfigs.getContent(), Channel.class));
    }
}
