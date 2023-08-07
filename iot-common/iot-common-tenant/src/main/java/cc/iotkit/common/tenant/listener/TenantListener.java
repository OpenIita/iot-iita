package cc.iotkit.common.tenant.listener;


import cc.iotkit.common.satoken.utils.LoginHelper;
import cc.iotkit.common.tenant.dao.TenantAware;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 * 类描述...
 *
 * @author Tiger Chen
 * created on 2023/7/14 20:50
 */

@Slf4j
public class TenantListener {

    @PreUpdate
    @PreRemove
    @PrePersist
    public void setTenant(TenantAware entity) {
        final String tenantId = LoginHelper.getTenantId();
        if (!"000000".equals(tenantId) && tenantId != null) {
            entity.setTenantId(tenantId);
        }
    }
}
