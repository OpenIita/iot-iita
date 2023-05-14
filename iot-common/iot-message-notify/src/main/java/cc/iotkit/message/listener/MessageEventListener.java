package cc.iotkit.message.listener;

import cc.iotkit.message.enums.MessageTypeEnum;
import cc.iotkit.message.model.MessageSend;
import cc.iotkit.model.notify.ChannelConfig;

import java.util.Map;

/**
 * author: 石恒
 * date: 2023-05-08 15:08
 * description:
 **/
public interface MessageEventListener {

    void doEvent(MessageSend message);

    default String getContentFormat(Map<String, String> param, String content) {
        for (String key : param.keySet()) {
            content = content.replaceAll("${" + key + "}", param.get(key));
        }
        return content;
    }

    ChannelConfig getChannelConfig(String channelConfigId);

    String addNotifyMessage(String content, MessageTypeEnum messageType);
}
