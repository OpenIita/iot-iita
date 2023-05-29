package cc.iotkit.message.listener;

import cc.iotkit.message.config.VertxManager;
import cc.iotkit.message.model.DingTalkMessage;
import cc.iotkit.message.model.Message;
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
public class DingTalkEventListener implements MessageEventListener {
    private String baseUrl = "https://oapi.dingtalk.com/robot/send?access_token=%s";

    @Override
    @EventListener(condition = "message.channel()='DingTalk'")
    public void doEvent(Message message) {
        WebClient client = WebClient.create(VertxManager.INSTANCE.getVertx());
        String url = String.format(baseUrl, message.getKey());
        DingTalkMessage qyWechatMessage = DingTalkMessage.builder()
                .msgtype("text")
                .text(DingTalkMessage.MessageContent.builder().content(getContent(message)).build())
                .build();
        client.post(url).sendJson(qyWechatMessage)
                .onSuccess(response -> log.info("Received response with status code" + response.statusCode()))
                .onFailure(err -> log.error("Something went wrong " + err.getMessage()));
    }
}
