package cc.iotkit.common.tenant.dao;


import cc.iotkit.common.tenant.listener.TenantListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 类描述...
 *
 * @author Tiger Chen
 * created on 2023/7/14 20:47
 */

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@EntityListeners(TenantListener.class)
public abstract class AbstractBaseEntity implements TenantAware, Serializable {
    private static final long serialVersionUID = 1L;

    @Size(max = 64)
    @Column(name = "tenant_id")
    private String tenantId;

    public AbstractBaseEntity(String tenantId) {
        this.tenantId = tenantId;
    }

}

