/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.modbus.model;

import cc.iotkit.model.modbus.ModbusThingModel;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import io.github.linpeilie.annotations.ReverseAutoMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Description: modbus点表物模型
 * @Author: ZOUZDC
 * @Date: 2024/4/28 22:52
 */
@Data
@Entity
@Table(name = "modbus_thing_model")
@ApiModel(value = "modbus物模型")
@AutoMapper(target = ModbusThingModel.class)
public class TbModbusThingModel  {
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "产品key")
    private String productKey;

    @ApiModelProperty(value = "模型内容")
    @Column(columnDefinition = "text")
    @AutoMapping(ignore = true)
    @ReverseAutoMapping(ignore = true)
    private String model;

    @ApiModelProperty(value = "更新时间")
    private Long updateAt;

}
