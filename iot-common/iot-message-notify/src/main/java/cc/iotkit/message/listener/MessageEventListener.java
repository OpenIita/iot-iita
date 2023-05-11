package cc.iotkit.message.listener;

import cc.iotkit.message.model.Message;

import java.util.Map;

/**
 * author: 石恒
 * date: 2023-05-08 15:08
 * description:
 **/
public interface MessageEventListener {

    void doEvent(Message message);

    default String getContent(Message message) {
        String content = message.getContent();
        Map<String, String> param = message.getParam();
        for (String key : param.keySet()) {
            content = content.replaceAll("${" + key + "}", param.get(key));
        }
        return content;
    }
}
