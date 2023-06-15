package cc.iotkit.manager.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.data.manager.IChannelConfigData;
import cc.iotkit.data.manager.IChannelData;
import cc.iotkit.data.manager.IChannelTemplateData;
import cc.iotkit.manager.dto.bo.channel.ChannelConfigBo;
import cc.iotkit.manager.dto.vo.channel.ChannelConfigVo;
import cc.iotkit.model.notify.Channel;
import cc.iotkit.model.notify.ChannelConfig;
import cc.iotkit.model.notify.ChannelTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * author: 石恒
 * date: 2023-05-11 15:21
 * description:
 **/
@Slf4j
@Service
public class NotifyService {

    @Resource
    private IChannelData iChannelData;

    @Resource
    private IChannelConfigData iChannelConfigData;

    @Resource
    private IChannelTemplateData iChannelTemplateData;

    public List<Channel> getChannelList() {
        return iChannelData.findAll();
    }

    public Paging<ChannelConfigVo> getChannelConfigList(PageRequest<ChannelConfigBo> request) {
        return iChannelConfigData.findAll(request.to(ChannelConfig.class)).to(ChannelConfigVo.class);
    }

    public ChannelConfig addChannelConfig(ChannelConfig channelConfig) {
        return iChannelConfigData.save(channelConfig);
    }

    public ChannelConfig getChannelConfigById(String id) {
        return iChannelConfigData.findById(id);
    }

    public ChannelConfig updateChannelConfigById(ChannelConfig channelConfig) {
        return iChannelConfigData.save(channelConfig);
    }

    public Boolean delChannelConfigById(String id) {
        iChannelConfigData.deleteById(id);
        return Boolean.TRUE;
    }

    public  List<ChannelTemplate> getChannelTemplateList() {
        List<ChannelTemplate> channelTemplateList = iChannelTemplateData.findAll();
        return channelTemplateList;
    }

    public ChannelTemplate addChannelTemplate(ChannelTemplate channelTemplate) {
        return iChannelTemplateData.save(channelTemplate);
    }

    public ChannelTemplate getChannelTemplateById(String id) {
        return iChannelTemplateData.findById(id);
    }

    public ChannelTemplate updateChannelTemplateById(ChannelTemplate channelTemplate) {
        return iChannelTemplateData.save(channelTemplate);
    }

    public Boolean delChannelTemplateById(String id) {
        iChannelTemplateData.deleteById(id);
        return Boolean.TRUE;
    }
}
