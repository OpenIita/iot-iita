package cc.iotkit.common.tenant.listener;

import cc.iotkit.common.tenant.dao.entiry.TenantAware;
import cc.iotkit.common.tenant.util.TenantContext;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class TenantListener {
    @PreUpdate
    @PreRemove
    @PrePersist
    public void setTenant(TenantAware entity) {
        final String tenantId = TenantContext.getTenantId();
        entity.setTenantId(tenantId);
    }
}
