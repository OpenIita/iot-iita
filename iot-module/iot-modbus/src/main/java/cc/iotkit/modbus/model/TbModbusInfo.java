package cc.iotkit.modbus.model;

import cc.iotkit.model.modbus.ModbusInfo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
public class TbModbusInfo {
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
}
