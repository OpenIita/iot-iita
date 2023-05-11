package cc.iotkit.manager.service;

import cc.iotkit.data.IChannelConfigData;
import cc.iotkit.data.IChannelData;
import cc.iotkit.data.IChannelTemplateData;
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

    public void addChannelConfig() {
        ChannelConfig channelConfig = ChannelConfig.builder()
                .build();
        iChannelConfigData.add(channelConfig);
    }

    public ChannelConfig getChannelConfigById(String id) {
        return iChannelConfigData.findById(id);
    }

    public ChannelConfig updateChannelConfigById(ChannelConfig channelConfig) {
        return iChannelConfigData.save(channelConfig);
    }

    public void delChannelConfigById(String id) {
        iChannelConfigData.deleteById(id);
    }

    public void getChannelTemplateList() {

    }

    public void addChannelTemplate() {

    }

    public ChannelTemplate getChannelTemplateById(String id) {
        return iChannelTemplateData.findById(id);
    }

    public void updateChannelTemplateById(ChannelTemplate channelTemplate) {
        iChannelTemplateData.save(channelTemplate);
    }

    public void delChannelTemplateById(String id) {
        iChannelTemplateData.deleteById(id);
    }
}
