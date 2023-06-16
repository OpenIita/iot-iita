package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IChannelConfigData;
import cc.iotkit.data.dao.ChannelConfigRepository;
import cc.iotkit.data.model.ChannelConfigMapper;
import cc.iotkit.data.model.TbChannelConfig;
import cc.iotkit.common.api.Paging;
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
import java.util.stream.Collectors;

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

    @Resource
    private ChannelConfigMapper channelConfigMapper;

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
        return channelConfigMapper.toDto(channelConfigRepository.findById(id).orElse(null));
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
        channelConfigRepository.save(channelConfigMapper.toVo(data));
        return data;
    }

    @Override
    public List<ChannelConfig> findAll() {
        return channelConfigRepository.findAll().stream().map(channelConfigMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Paging<ChannelConfig> findAll(PageRequest<ChannelConfig> pageRequest) {
        Page<TbChannelConfig> tbChannelConfigs = channelConfigRepository.findAll(Pageable.ofSize(pageRequest.getPageSize()).withPage(pageRequest.getPageNum() - 1));
        return new Paging<>(
                tbChannelConfigs.getTotalElements(),
                tbChannelConfigs.getContent()
                        .stream().map(channelConfigMapper::toDto)
                        .collect(Collectors.toList())
        );
    }
}
