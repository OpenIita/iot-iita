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

import cc.iotkit.model.protocol.ProtocolConverter;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "protocol_converter")
@AutoMapper(target = ProtocolConverter.class)
public class TbProtocolConverter {

    @Id
    private String id;

    /**
     * 所属性用户id
     */
    @ApiModelProperty(value = "所属性用户id")
    private String uid;

    @ApiModelProperty(value = "转换器名称")
    private String name;

    @Column(name = "[desc]")
    @ApiModelProperty(value = "描述")
    private String desc;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

    @ApiModelProperty(value = "转换脚本类型")
    private String typ;

    // 脚本内容
    @Column(columnDefinition = "text")//设置映射为text类型
    @ApiModelProperty(value = "脚本内容")
    private String script;

}
