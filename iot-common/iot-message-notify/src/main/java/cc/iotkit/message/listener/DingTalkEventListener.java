package cc.iotkit.message.listener;

import cc.iotkit.data.IChannelConfigData;
import cc.iotkit.data.IChannelTemplateData;
import cc.iotkit.message.config.VertxManager;
import cc.iotkit.message.enums.MessageTypeEnum;
import cc.iotkit.message.model.DingTalkMessage;
import cc.iotkit.message.model.MessageSend;
import cc.iotkit.model.notify.ChannelConfig;
import io.vertx.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * author: 石恒
 * date: 2023-05-08 15:09
 * description:
 **/
@Slf4j
@Component
public class DingTalkEventListener implements MessageEventListener {
    private String baseUrl = "https://oapi.dingtalk.com/robot/send?access_token=%s";

    @Resource
    private IChannelConfigData iChannelConfigData;
    @Resource
    private IChannelTemplateData iChannelTemplateData;

    @Override
    @EventListener(condition = "message.code='DingTalk'")
    public void doEvent(MessageSend message) {
        WebClient client = WebClient.create(VertxManager.INSTANCE.getVertx());

        ChannelConfig channelConfig = getChannelConfig(message.getChannelTemplate().getChannelConfigId());
        ChannelConfig.ChannelParam param = channelConfig.getParam();

        String url = String.format(baseUrl, param.getDingTalkAccessToken());
        DingTalkMessage qyWechatMessage = DingTalkMessage.builder()
                .msgtype("text")
                .text(DingTalkMessage.MessageContent.builder().content(getContentFormat(message.getParam(), message.getChannelTemplate().getContent())).build())
                .build();
        client.post(url).sendJson(qyWechatMessage)
                .onSuccess(response -> log.info("Received response with status code" + response.statusCode()))
                .onFailure(err -> log.error("Something went wrong " + err.getMessage()));
    }

    @Override
    public ChannelConfig getChannelConfig(String channelConfigId) {
        return iChannelConfigData.findById(channelConfigId);
    }

    @Override
    public String addNotifyMessage(String content, MessageTypeEnum messageType) {
        return null;
    }
}
