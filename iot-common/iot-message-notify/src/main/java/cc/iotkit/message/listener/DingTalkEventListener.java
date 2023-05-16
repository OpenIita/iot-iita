package cc.iotkit.message.listener;

import cc.iotkit.data.IChannelConfigData;
import cc.iotkit.data.INotifyMessageData;
import cc.iotkit.message.config.VertxManager;
import cc.iotkit.message.enums.MessageTypeEnum;
import cc.iotkit.message.event.MessageEvent;
import cc.iotkit.message.model.DingTalkMessage;
import cc.iotkit.message.model.MessageSend;
import cc.iotkit.message.util.DingTalkUtil;
import cc.iotkit.model.notify.ChannelConfig;
import cc.iotkit.model.notify.NotifyMessage;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import io.vertx.ext.web.client.WebClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * author: 石恒
 * date: 2023-05-08 15:09
 * description:
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class DingTalkEventListener implements MessageEventListener {
    private final IChannelConfigData iChannelConfigData;
    private final INotifyMessageData iNotifyMessageData;

    @Override
    @EventListener(condition = "#messageEvent.message.code=='DingTalk'")
    public void doEvent(MessageEvent messageEvent) {
        MessageSend message = messageEvent.getMessage();
        ChannelConfig channelConfig = getChannelConfig(message.getChannelTemplate().getChannelConfigId());
        ChannelConfig.ChannelParam param = channelConfig.getParam();
        String content = getContentFormat(message.getParam(), message.getChannelTemplate().getContent());
        try {
            Long timestamp = System.currentTimeMillis();
            String sign = DingTalkUtil.getSign(param.getDingTalkSecret(), timestamp);
            String url = param.getDingTalkWebhook() + "&timestamp=" + timestamp + "&sign=" + sign;
            NotifyMessage notifyMessage = addNotifyMessage(content, message.getMessageType());
            DingTalkClient client = new DefaultDingTalkClient(url);
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("markdown");
            OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
            markdown.setTitle(getContentFormat(message.getParam(), channelConfig.getTitle()));
            markdown.setText(content);
            request.setMarkdown(markdown);
            OapiRobotSendResponse response = client.execute(request);
            notifyMessage.setStatus(Boolean.TRUE);
            iNotifyMessageData.save(notifyMessage);
        } catch (Exception e) {
            log.error("DingTalk send message error " + e);
        }

    }

    @Override
    public ChannelConfig getChannelConfig(String channelConfigId) {
        return iChannelConfigData.findById(channelConfigId);
    }

    @Override
    public NotifyMessage addNotifyMessage(String content, MessageTypeEnum messageType) {
        return iNotifyMessageData.add(NotifyMessage.builder()
                .content(content)
                .messageType(messageType.getCode())
                .status(Boolean.FALSE)
                .build());
    }
}
