package cc.iotkit.manager.service;

import cc.iotkit.data.manager.IChannelConfigData;
import cc.iotkit.data.manager.IChannelData;
import cc.iotkit.data.manager.IChannelTemplateData;
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

    public List<ChannelConfig> getChannelConfigList() {
        return iChannelConfigData.findAll();
    }

    public ChannelConfig addChannelConfig(ChannelConfig channelConfig) {
        return iChannelConfigData.add(channelConfig);
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
        return iChannelTemplateData.add(channelTemplate);
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
