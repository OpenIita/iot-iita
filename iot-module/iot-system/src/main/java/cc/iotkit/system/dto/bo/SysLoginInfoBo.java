package cc.iotkit.system.dto.bo;

import cc.iotkit.model.system.SysLoginInfo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统访问记录业务对象 sys_logininfor
 *
 * @author Michelle.Chung
 */

@Data
@AutoMapper(target = SysLoginInfo.class, reverseConvertGenerate = false)
public class SysLoginInfoBo {

    /**
     * 访问ID
     */
    private Long id;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录状态（0成功 1失败）
     */
    private String status;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 访问时间
     */
    private Date loginTime;

    /**
     * 请求参数
     */
    private Map<String, Object> params = new HashMap<>();


}
