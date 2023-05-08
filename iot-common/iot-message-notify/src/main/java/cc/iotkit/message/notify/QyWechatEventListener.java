package cc.iotkit.message.notify;

import cc.iotkit.message.model.Message;
import cc.iotkit.message.model.QyWechatMessage;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * author: 石恒
 * date: 2023-05-08 15:09
 * description:
 **/
@Slf4j
@Component
public class QyWechatEventListener implements EventListener {

    private String baseUrl = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s";

    @Override
    public void doEvent(Message message) {
        WebClient client = WebClient.create(Vertx.vertx());
        String url = String.format(baseUrl, message.getKey());
        QyWechatMessage qyWechatMessage = QyWechatMessage.builder()
                .msgtype("text")
                .text(QyWechatMessage.MessageContent.builder().content(message.getContent()).build())
                .build();
        client.post(url).sendJson(qyWechatMessage, rs -> {
            if (rs.succeeded()) {
                log.info("发送成功.");
            }
            if (rs.failed()) {
                log.info("发送失败.");
            }
        });
    }
}
