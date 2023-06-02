package cc.iotkit.web.domain.vo;

import cc.iotkit.system.dto.vo.SysTenantVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;


/**
 * 租户列表
 *
 * @author Lion Li
 */
@Data
@AutoMapper(target = SysTenantVo.class)
public class TenantListVo {

    private String tenantId;

    private String companyName;

    private String domain;

}
