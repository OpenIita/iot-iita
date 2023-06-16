package cc.iotkit.model.notify;

import cc.iotkit.model.Id;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * author: 石恒
 * date: 2023-05-11 16:30
 * description:
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelConfig implements Id<Long> {

    private Long id;

    private Long channelId;

    private String title;

    private ChannelParam param;

    private Long createAt;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChannelParam implements Serializable {
        private String userName;
        private String passWord;
        private String host;
        private Integer port;
        private Boolean mailSmtpAuth;
        private String from;
        private String to;
        private String dingTalkWebhook;
        private String dingTalkSecret;
        private String qyWechatWebhook;

    }
}
