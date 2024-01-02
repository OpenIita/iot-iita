package cc.iotkit.message.listener;

import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.message.config.VertxManager;
import cc.iotkit.message.event.MessageEvent;
import cc.iotkit.message.model.Message;
import cc.iotkit.message.model.QyWechatConfig;
import cc.iotkit.message.model.QyWechatMessage;
import io.vertx.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * author: 石恒
 * date: 2023-05-08 15:09
 * description:
 **/
@Slf4j
@Component
public class QyWechatEventListener implements MessageEventListener {

    @Override
    @EventListener(classes = MessageEvent.class, condition = "#event.message.channel=='QyWechat'")
    public void doEvent(MessageEvent event) {
        Message message = event.getMessage();
        String channelConfig = message.getChannelConfig();
        QyWechatConfig qyWechatConfig = JsonUtils.parse(channelConfig, QyWechatConfig.class);

        WebClient client = WebClient.create(VertxManager.INSTANCE.getVertx());
        QyWechatMessage qyWechatMessage = QyWechatMessage.builder()
                .msgtype("text")
                .text(QyWechatMessage.MessageContent.builder().content(message.getFormatContent()).build())
                .build();
        client.post(qyWechatConfig.getQyWechatWebhook()).sendJson(qyWechatMessage)
                .onSuccess(response -> log.info("Received response with status code" + response.statusCode()))
                .onFailure(err -> log.error("Something went wrong " + err.getMessage()));
    }
}
