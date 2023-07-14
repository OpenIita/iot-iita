package cc.iotkit.common.tenant.listener;


import cc.iotkit.common.satoken.utils.LoginHelper;
import cc.iotkit.common.tenant.dao.TenantAware;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 * 类描述...
 *
 * @author Tiger Chen
 * created on 2023/7/14 20:50
 */

public class TenantListener {

    @PreUpdate
    @PreRemove
    @PrePersist
    public void setTenant(TenantAware entity) {

        final String tenantId = LoginHelper.getTenantId();
        entity.setTenantId(tenantId);
    }
}
