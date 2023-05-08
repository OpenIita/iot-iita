package cc.iotkit.message.notify;

import cc.iotkit.message.model.DingTalkMessage;
import cc.iotkit.message.model.Message;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

/**
 * author: 石恒
 * date: 2023-05-08 15:09
 * description:
 **/
@Slf4j
public class DingTalkEventListener implements EventListener{
    private String baseUrl = "https://oapi.dingtalk.com/robot/send?access_token=%s";
    @Override
    public void doEvent(Message message) {
        WebClient client = WebClient.create(Vertx.vertx());
        String url = String.format(baseUrl, message.getKey());
        DingTalkMessage qyWechatMessage = DingTalkMessage.builder()
                .msgtype("text")
                .text(DingTalkMessage.MessageContent.builder().content(message.getContent()).build())
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
