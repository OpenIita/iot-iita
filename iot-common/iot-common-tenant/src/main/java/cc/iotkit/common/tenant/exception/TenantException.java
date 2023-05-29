package cc.iotkit.common.tenant.exception;


import cc.iotkit.common.exception.BizException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户异常类
 *
 * @author Lion Li
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantException extends BizException {

    public TenantException(String code, String message) {
        super("tenant", code, message);
    }
}
