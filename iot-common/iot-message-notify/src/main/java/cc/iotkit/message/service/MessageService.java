package cc.iotkit.message.service;

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.data.IChannelTemplateData;
import cc.iotkit.message.event.MessageEvent;
import cc.iotkit.message.model.Message;
import cc.iotkit.message.model.MessageSend;
import cc.iotkit.model.notify.ChannelTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * author: 石恒
 * date: 2023-05-08 16:02
 * description:
 **/
@Service
public class MessageService {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private IChannelTemplateData iChannelTemplateData;

    public void sendMessage(Message message) {

        ChannelTemplate channelTemplate = iChannelTemplateData.findById(message.getChannelTemplateId());
        if (Objects.isNull(channelTemplate)) {
            throw new BizException(ErrCode.RECORD_NOT_FOUND);
        }

        MessageSend messageSend = MessageSend.builder()
                .messageType(message.getMessageType())
                .param(message.getParam())
                .channelTemplate(channelTemplate)
                .code(channelTemplate.getChannelCode())
                .build();
        applicationEventPublisher.publishEvent(new MessageEvent(messageSend));
    }

}
