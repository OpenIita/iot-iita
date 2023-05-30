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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "task_info")
public class TbTaskInfo {

    @Id
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "任务名称")
    private String name;

    /**
     * 任务类型
     */
    @ApiModelProperty(value = "任务类型")
    private String type;

    /**
     * 表达式
     * 定时器使用cron表达式
     * 延时器使用延时时长（秒）
     */
    @ApiModelProperty(value = "表达式")
    private String expression;

    /**
     * 描述
     */
    @Column(name = "[desc]")
    @ApiModelProperty(value = "描述")
    private String desc;

    /**
     * 任务输出
     */
    @Column(columnDefinition = "text")
    @ApiModelProperty(value = "任务输出")
    private String actions;

    /**
     * 任务状态
     */
    @ApiModelProperty(value = "任务状态")
    private String state;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String uid;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

    /**
     * 操作备注
     */
    @ApiModelProperty(value = "操作备注")
    private String reason;

}
