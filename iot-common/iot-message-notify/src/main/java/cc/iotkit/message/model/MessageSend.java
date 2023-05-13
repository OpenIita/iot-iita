package cc.iotkit.message.model;

import cc.iotkit.message.enums.MessageTypeEnum;
import cc.iotkit.model.notify.ChannelTemplate;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: 石恒
 * @Date: 2023/5/13 21:43
 * @Description:
 */
@Data
@Builder
public class MessageSend implements Serializable {
    private MessageTypeEnum messageType;
    private Map<String, String> param;
    private String code;
    private ChannelTemplate channelTemplate;
}
