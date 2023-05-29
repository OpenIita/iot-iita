package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.manager.IChannelTemplateData;
import cc.iotkit.data.dao.ChannelTemplateRepository;
import cc.iotkit.data.model.TbChannelTemplate;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.notify.ChannelTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
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
        return MapstructUtils.convert(channelTemplateRepository.findById(id).orElse(null), ChannelTemplate.class);
    }

    @Override
    public List<ChannelTemplate> findByIds(Collection<String> id) {
        return null;
    }

    @Override
    public ChannelTemplate save(ChannelTemplate data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        channelTemplateRepository.save(MapstructUtils.convert(data, TbChannelTemplate.class));
        return data;
    }

    @Override
    public void batchSave(List<ChannelTemplate> data) {

    }


    @Override
    public void deleteById(String id) {
        channelTemplateRepository.deleteById(id);
    }

    @Override
    public void deleteByIds(Collection<String> strings) {

    }



    @Override
    public long count() {
        return channelTemplateRepository.count();
    }

    @Override
    public List<ChannelTemplate> findAll() {
        return channelTemplateRepository.findAll().stream()
                .map(c -> MapstructUtils.convert(c, ChannelTemplate.class))
                .collect(Collectors.toList());
    }

    @Override
    public Paging<ChannelTemplate> findAll(PageRequest<ChannelTemplate> pageRequest) {
       return null;
    }

    @Override
    public List<ChannelTemplate> findAllByCondition(ChannelTemplate data) {
        return null;
    }

    @Override
    public ChannelTemplate findOneByCondition(ChannelTemplate data) {
        return null;
    }


}
