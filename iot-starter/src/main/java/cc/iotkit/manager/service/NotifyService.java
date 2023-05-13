package cc.iotkit.manager.service;

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.data.IChannelConfigData;
import cc.iotkit.data.IChannelData;
import cc.iotkit.data.IChannelTemplateData;
import cc.iotkit.data.INotifyMessageData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.notify.Channel;
import cc.iotkit.model.notify.ChannelConfig;
import cc.iotkit.model.notify.ChannelTemplate;
import cc.iotkit.model.notify.NotifyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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

    @Resource
    private INotifyMessageData iNotifyMessageData;

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

    public List<ChannelTemplate> getChannelTemplateList() {
        return iChannelTemplateData.findAll();
    }

    public ChannelTemplate addChannelTemplate(ChannelTemplate channelTemplate) {
        channelTemplate.setChannelCode(getChannelCodeByChannelConfigId(channelTemplate.getChannelConfigId()));
        return iChannelTemplateData.add(channelTemplate);
    }

    public ChannelTemplate getChannelTemplateById(String id) {
        return iChannelTemplateData.findById(id);
    }

    public ChannelTemplate updateChannelTemplateById(ChannelTemplate channelTemplate) {
        channelTemplate.setChannelCode(getChannelCodeByChannelConfigId(channelTemplate.getChannelConfigId()));
        return iChannelTemplateData.save(channelTemplate);
    }

    private String getChannelCodeByChannelConfigId(String channelConfigId) {
        ChannelConfig channelConfig = iChannelConfigData.findById(channelConfigId);
        if (Objects.isNull(channelConfig)) {
            throw new BizException(ErrCode.RECORD_NOT_FOUND);
        }
        Channel channel = iChannelData.findById(channelConfig.getChannelId());
        return channel.getCode();
    }

    public Boolean delChannelTemplateById(String id) {
        iChannelTemplateData.deleteById(id);
        return Boolean.TRUE;
    }

    public Paging<NotifyMessage> getNotifyMessageList(int page, int size) {
        return iNotifyMessageData.findAll(page, size);
    }
}
