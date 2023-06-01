package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IChannelData;
import cc.iotkit.data.dao.ChannelRepository;
import cc.iotkit.data.model.TbChannel;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.notify.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * author: 石恒
 * date: 2023-05-11 17:44
 * description:
 **/
@Primary
@Service
public class ChannelDataImpl implements IChannelData, IJPACommData<Channel, String> {

    @Resource
    private ChannelRepository channelRepository;

    @Override
    public JpaRepository getBaseRepository() {
        return channelRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbChannel.class;
    }

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
    public List<Channel> findAll() {
        return MapstructUtils.convert(channelRepository.findAll(), Channel.class);
    }



}
