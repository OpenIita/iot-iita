package cc.iotkit.message.model;

import lombok.Data;

import java.util.Map;

/**
 * author: 石恒
 * date: 2023-05-08 15:15
 * description:
 **/
@Data
public class Message {
    private String key;
    private String content;
    private Map<String, String> param;
    private Long channelId;
    private String channel;
}
