package cc.iotkit.data.model;

import cc.iotkit.model.system.SysLogininfor;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 系统访问记录表 sys_logininfor
 *
 * @author Lion Li
 */

@Data
@Entity
@Table(name = "sys_logininfor")
@AutoMapper(target = SysLogininfor.class)
public class TbSysLogininfor extends BaseEntity {
    /**
     * ID
     */
    @Id
    @ApiModelProperty(value = "ID")
    private Long id;

    /**
     * 租户编号
     */
    @ApiModelProperty(value = "租户编号")
    private String tenantId;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String userName;

    /**
     * 登录状态 0成功 1失败
     */
    @ApiModelProperty(value = "登录状态 0成功 1失败")
    private String status;

    /**
     * 登录IP地址
     */
    @ApiModelProperty(value = "登录IP地址")
    private String ipaddr;

    /**
     * 登录地点
     */
    @ApiModelProperty(value = "登录地点")
    private String loginLocation;

    /**
     * 浏览器类型
     */
    @ApiModelProperty(value = "浏览器类型")
    private String browser;

    /**
     * 操作系统
     */
    @ApiModelProperty(value = "操作系统")
    private String os;

    /**
     * 提示消息
     */
    @ApiModelProperty(value = "提示消息")
    private String msg;

    /**
     * 访问时间
     */
    @ApiModelProperty(value = "访问时间")
    private Date loginTime;

}
