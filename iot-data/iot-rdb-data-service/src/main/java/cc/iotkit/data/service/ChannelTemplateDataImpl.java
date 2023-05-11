package cc.iotkit.data.service;

import cc.iotkit.data.IChannelTemplateData;
import cc.iotkit.data.dao.ChannelTemplateRepository;
import cc.iotkit.data.model.ChannelTemplateMapper;
import cc.iotkit.data.model.TbChannelTemplate;
import cc.iotkit.model.Paging;
import cc.iotkit.model.notify.ChannelTemplate;
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
 * date: 2023-05-11 17:45
 * description:
 **/
@Primary
@Service
public class ChannelTemplateDataImpl implements IChannelTemplateData {

    @Resource
    private ChannelTemplateRepository channelTemplateRepository;

    @Override
    public ChannelTemplate findById(String id) {
        return ChannelTemplateMapper.M.toDto(channelTemplateRepository.findById(id).orElse(null));
    }

    @Override
    public ChannelTemplate save(ChannelTemplate data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        channelTemplateRepository.save(ChannelTemplateMapper.M.toVo(data));
        return data;
    }

    @Override
    public ChannelTemplate add(ChannelTemplate data) {
        data.setCreateAt(System.currentTimeMillis());
        return save(data);
    }

    @Override
    public void deleteById(String id) {
        channelTemplateRepository.deleteById(id);
    }

    @Override
    public long count() {
        return channelTemplateRepository.count();
    }

    @Override
    public List<ChannelTemplate> findAll() {
        return channelTemplateRepository.findAll().stream().map(ChannelTemplateMapper.M::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Paging<ChannelTemplate> findAll(int page, int size) {
        Page<TbChannelTemplate> tbDeviceConfigs = channelTemplateRepository.findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(
                tbDeviceConfigs.getTotalElements(),
                tbDeviceConfigs.getContent()
                        .stream().map(ChannelTemplateMapper.M::toDto)
                        .collect(Collectors.toList())
        );
    }
}
