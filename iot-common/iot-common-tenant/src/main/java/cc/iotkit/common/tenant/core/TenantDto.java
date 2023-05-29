package cc.iotkit.common.tenant.core;

import cc.iotkit.common.api.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户基类
 *
 * @author Michelle.Chung
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantDto extends BaseDto {

    /**
     * 租户编号
     */
    private String tenantId;

}
