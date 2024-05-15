/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

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
    WebClient client = WebClient.create(VertxManager.INSTANCE.getVertx());

    @Override
    @EventListener(classes = MessageEvent.class, condition = "#event.message.channel=='QyWechat'")
    public void doEvent(MessageEvent event) {
        Message message = event.getMessage();
        String channelConfig = message.getChannelConfig();
        QyWechatConfig qyWechatConfig = JsonUtils.parse(channelConfig, QyWechatConfig.class);

        QyWechatMessage qyWechatMessage = QyWechatMessage.builder()
                .msgtype("text")
                .text(QyWechatMessage.MessageContent.builder().content(message.getFormatContent()).build())
                .build();

        client.postAbs(qyWechatConfig.getQyWechatWebhook()).sendJson(qyWechatMessage)
                .onSuccess(response -> log.info("Received response with status code" + response.statusCode()))
                .onFailure(err -> log.error("Something went wrong " + err.getMessage()));
    }
}
