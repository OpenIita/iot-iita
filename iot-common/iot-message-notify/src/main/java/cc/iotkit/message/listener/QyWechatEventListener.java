/*
package cc.iotkit.message.listener;

import cc.iotkit.message.config.VertxManager;
import cc.iotkit.message.model.Message;
import cc.iotkit.message.model.QyWechatMessage;
import io.vertx.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

*/
/**
 * author: 石恒
 * date: 2023-05-08 15:09
 * description:
 **//*

@Slf4j
@Component
public class QyWechatEventListener implements MessageEventListener {

    private String baseUrl = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s";

    @Override
    @EventListener(condition = "message.channel()='QyWechat'")
    public Boolean doEvent(Message message) {
        WebClient client = WebClient.create(VertxManager.INSTANCE.getVertx());
        String url = String.format(baseUrl, message.getKey());
        QyWechatMessage qyWechatMessage = QyWechatMessage.builder()
                .msgtype("text")
                .text(QyWechatMessage.MessageContent.builder().content(getContent(message)).build())
                .build();
        client.post(url).sendJson(qyWechatMessage)
                .onSuccess(response -> log.info("Received response with status code" + response.statusCode()))
                .onFailure(err -> log.error("Something went wrong " + err.getMessage()));
        return Boolean.TRUE;
    }
}
*/
