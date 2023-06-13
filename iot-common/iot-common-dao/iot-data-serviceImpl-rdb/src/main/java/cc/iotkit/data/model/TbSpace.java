/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data.model;

import cc.iotkit.model.space.Space;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@ApiModel(value = "空间")
@Table(name = "space")
@AutoMapper(target = Space.class)
public class TbSpace {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "空间id")
    private String id;

    /**
     * 关联家庭id
     */
    @ApiModelProperty(value = "关联家庭id")
    private String homeId;

    /**
     * 关联用户id
     */
    @ApiModelProperty(value = "关联用户id")
    private String uid;

    /**
     * 空间名称
     */
    @ApiModelProperty(value = "空间名称")
    private String name;

    /**
     * 设备数量
     */
    @ApiModelProperty(value = "设备数量")
    private Integer deviceNum;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

}
