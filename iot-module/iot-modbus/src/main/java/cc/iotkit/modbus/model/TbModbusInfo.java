package cc.iotkit.modbus.model;

import cc.iotkit.common.tenant.dao.TenantAware;
import cc.iotkit.common.tenant.listener.TenantListener;
import cc.iotkit.model.modbus.ModbusInfo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

/**
 * @Description: modbus点表信息
 * @Author: ZOUZDC
 * @Date: 2024/4/29 0:11
 */
@Data
@Entity
@Table(name = "modbus_info")
@ApiModel(value = "modbus点表信息")
@AutoMapper(target = ModbusInfo.class)
@EntityListeners(TenantListener.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "long")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TbModbusInfo implements TenantAware {
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 配置所属用户
     */
    @ApiModelProperty(value = "配置所属用户")
    private String uid;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String name;

    @ApiModelProperty(value = "productKey")
    private String productKey;

    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long updateAt;


    @Column(name = "tenant_id")
    private Long tenantId;
}
