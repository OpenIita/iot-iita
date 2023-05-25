package cc.iotkit.common.log.event;

import cn.hutool.http.useragent.UserAgent;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录事件
 *
 * @author Lion Li
 */

@Data
public class LogininforEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 登录状态 0成功 1失败
     */
    private String status;

    /**
     * 提示消息
     */
    private String message;

    /**
     * ip
     */
    private String ip;

    /**
     * user-agent
     */
    private UserAgent userAgent;

    /**
     * 其他参数
     */
    private Object[] args;

}
