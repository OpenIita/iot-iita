package cc.iotkit.data.service;

import cc.iotkit.data.manager.IChannelConfigData;
import cc.iotkit.data.dao.ChannelConfigRepository;
import cc.iotkit.data.convert.ChannelConfigMapper;
import cc.iotkit.data.model.TbChannelConfig;
import cc.iotkit.model.Paging;
import cc.iotkit.model.notify.ChannelConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class ChannelConfigDataImpl implements IChannelConfigData {

    @Resource
    private ChannelConfigRepository channelConfigRepository;

    @Override
    public ChannelConfig findById(String id) {
        return ChannelConfigMapper.M.toDto(channelConfigRepository.findById(id).orElse(null));
    }

    @Override
    public ChannelConfig save(ChannelConfig data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        channelConfigRepository.save(ChannelConfigMapper.M.toVo(data));
        return data;
    }

    @Override
    public ChannelConfig add(ChannelConfig data) {
        data.setCreateAt(System.currentTimeMillis());
        return save(data);
    }

    @Override
    public void deleteById(String id) {
        channelConfigRepository.deleteById(id);
    }

    @Override
    public long count() {
        return channelConfigRepository.count();
    }

    @Override
    public List<ChannelConfig> findAll() {
        return channelConfigRepository.findAll().stream().map(ChannelConfigMapper.M::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Paging<ChannelConfig> findAll(int page, int size) {
        Page<TbChannelConfig> tbDeviceConfigs = channelConfigRepository.findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(
                tbDeviceConfigs.getTotalElements(),
                tbDeviceConfigs.getContent()
                        .stream().map(ChannelConfigMapper.M::toDto)
                        .collect(Collectors.toList())
        );
    }
}
