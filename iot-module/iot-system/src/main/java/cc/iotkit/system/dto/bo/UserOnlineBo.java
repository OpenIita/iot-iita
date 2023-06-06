package cc.iotkit.system.dto.bo;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.common.constant.UserConstants;
import cc.iotkit.model.system.SysUser;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户信息业务对象 sys_user
 *
 * @author Michelle.Chung
 */

@Data
@NoArgsConstructor

@EqualsAndHashCode(callSuper = true)
public class UserOnlineBo extends BaseDto {


    /**
     * 用户账号
     */

    @ApiModelProperty(value = "用户账号")
    private String ipaddr;

    /**
     * 用户昵称
     */

    @ApiModelProperty("用户昵称")
    private String userName;


}
