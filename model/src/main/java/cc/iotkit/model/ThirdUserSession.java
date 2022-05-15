package cc.iotkit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 第三方接入用户会话
 */
@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdUserSession {

    /**
     * 账号id
     */
    @Id
    private String uid;

    /**
     * 账号类型
     */
    private String type;

    /**
     * 登录授权后的token
     */
    private String token;

    /**
     * 授权时间
     */
    private Long authAt;
}
