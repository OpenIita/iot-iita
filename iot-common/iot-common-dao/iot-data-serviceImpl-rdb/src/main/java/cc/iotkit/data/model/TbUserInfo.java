package cc.iotkit.data.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user_info")
public class TbUserInfo {

    @Id
    private String id;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String uid;

    /**
     * 归属账号
     */
    @ApiModelProperty(value = "归属账号")
    private String ownerId;

    /**
     * 密钥（密码加密后的内容）
     */
    @ApiModelProperty(value = "密钥（密码加密后的内容）")
    private String secret;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    /**
     * 性别 0-未知 1-male,2-female
     */
    @ApiModelProperty(value="性别 0-未知 1-male,2-female")
    private Integer gender;

    /**
     * 头像地址
     */
    @ApiModelProperty(value="头像地址")
    private String avatarUrl;

    @ApiModelProperty(value="email")
    private String email;

    @ApiModelProperty(value="地址")
    private String address;

    /**
     * 当前家庭Id
     */
    @ApiModelProperty(value="当前家庭Id")
    private String currHomeId;

    /**
     * 用户类型
     * 0:平台用户
     * 1:终端用户
     */
    @ApiModelProperty(value="用户类型 0:平台用户 1:终端用户")
    private Integer type;

    /**
     * 角色
     */
    @ApiModelProperty(value="角色")
    private String roles;

    /**
     * 权限
     */
    @ApiModelProperty(value="权限")
    private String permissions;

    /**
     * 用户使用的平台
     * 见:Constants.THIRD_PLATFORM
     */
    @ApiModelProperty(value="用户使用的平台")
    private String usePlatforms;

    @ApiModelProperty(value="创建时间")
    private Long createAt;

}
