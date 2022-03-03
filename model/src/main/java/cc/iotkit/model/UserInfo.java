package cc.iotkit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class UserInfo {

    public static final int USER_TYPE_PLATFORM = 0;
    public static final int USER_TYPE_CLIENT = 1;

    @Id
    private String id;

    /**
     * 用户账号
     */
    private String uid;

    /**
     * 归属账号
     */
    private String ownerId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 性别 0-未知 1-male,2-female
     */
    private Integer gender;

    /**
     * 头像地址
     */
    private String avatarUrl;

    private String email;

    private String address;

    /**
     * 当前家庭Id
     */
    private String currHomeId;

    /**
     * 用户类型
     * 0:平台用户
     * 1:终端用户
     */
    private Integer type;

    /**
     * 角色
     */
    private List<String> roles;

    /**
     * 用户使用的平台
     */
    private Platforms usePlatforms = new Platforms();

    private Long createAt;

    @Data
    public static class Platforms {
        /**
         * 天猫精灵
         */
        private boolean aligenie;
    }
}
