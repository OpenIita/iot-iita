package cc.iotkit.data.model;

import cc.iotkit.data.model.BaseEntity;
import cc.iotkit.model.system.SysConfig;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 参数配置表 sys_config
 *
 * @author Lion Li
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_config")
@AutoMapper(target = SysConfig.class)
public class TbSysConfig extends BaseEntity {

    /**
     * 参数主键
     */
    @Id
    private Long configId;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    private String configType;

    /**
     * 备注
     */
    private String remark;

}
