package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IChannelConfigData;
import cc.iotkit.data.dao.ChannelConfigRepository;
import cc.iotkit.data.model.TbChannelConfig;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.notify.Channel;
import cc.iotkit.model.notify.ChannelConfig;
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
 * date: 2023-05-11 17:43
 * description:
 **/
@Primary
@Service
public class ChannelConfigDataImpl implements IChannelConfigData, IJPACommData<ChannelConfig, String> {

    @Resource
    private ChannelConfigRepository channelConfigRepository;

    @Override
    public JpaRepository getBaseRepository() {
        return channelConfigRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbChannelConfig.class;
    }

    @Override
    public Class getTClass() {
        return ChannelConfig.class;
    }

    @Override
    public ChannelConfig findById(String id) {
        return MapstructUtils.convert(channelConfigRepository.findById(id).orElse(null), ChannelConfig.class);
    }

    @Override
    public List<ChannelConfig> findByIds(Collection<String> id) {
        return null;
    }

    @Override
    public ChannelConfig save(ChannelConfig data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        channelConfigRepository.save(MapstructUtils.convert(data, TbChannelConfig.class));
        return data;
    }


}
