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
    private String id;

    private String name;

    /**
     * 任务类型
     */
    private String type;

    /**
     * 表达式
     * 定时器使用cron表达式
     * 延时器使用延时时长（秒）
     */
    private String expression;

    /**
     * 描述
     */
    @Column(name = "[desc]")
    private String desc;

    /**
     * 任务输出
     */
    @Column(columnDefinition = "text")
    private String actions;

    /**
     * 任务状态
     */
    private String state;

    /**
     * 创建者
     */
    private String uid;

    private Long createAt;

    /**
     * 操作备注
     */
    private String reason;

}
