package cc.iotkit.message.model;

import cc.iotkit.message.enums.MessageTypeEnum;
import lombok.Data;

import java.util.Map;

/**
 * author: 石恒
 * date: 2023-05-08 15:15
 * description:
 **/
@Data
public class Message {
    private MessageTypeEnum messageType;
    private Map<String, String> param;
    private String channelTemplateId;
}
