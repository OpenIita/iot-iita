package cc.iotkit.system.domain.bo;

import cc.iotkit.common.domain.model.BaseEntity;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;
import cc.iotkit.model.system.SysTenantPackage;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 租户套餐业务对象 sys_tenant_package
 *
 * @author Michelle.Chung
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysTenantPackage.class, reverseConvertGenerate = false)
public class SysTenantPackageBo extends BaseEntity {

    /**
     * 租户套餐id
     */
    @NotNull(message = "租户套餐id不能为空", groups = {EditGroup.class})
    private Long packageId;

    /**
     * 套餐名称
     */
    @NotBlank(message = "套餐名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String packageName;

    /**
     * 关联菜单id
     */
    @AutoMapping(target = "menuIds", expression = "java(org.dromara.common.core.utils.StringUtils.join(source.getMenuIds(), \",\"))")
    private Long[] menuIds;

    /**
     * 备注
     */
    private String remark;

    /**
     * 菜单树选择项是否关联显示
     */
    private Boolean menuCheckStrictly;

    /**
     * 状态（0正常 1停用）
     */
    private String status;


}
