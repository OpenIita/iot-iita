package cc.iotkit.oauth.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoVo {

    /**
     * 用户账号
     */
    private String uid;

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
    private List<String> roles = new ArrayList<>();

    /**
     * 权限
     */
    private List<String> permissions = new ArrayList<>();

    /**
     * 用户使用的平台
     * 见:Constants.THIRD_PLATFORM
     */
    private List<String> usePlatforms = new ArrayList<>();


}
