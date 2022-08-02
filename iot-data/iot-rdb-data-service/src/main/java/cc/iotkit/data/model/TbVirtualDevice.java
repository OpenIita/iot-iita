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
@Table(name = "virtual_device")
public class TbVirtualDevice {

    @Id
    private String id;

    /**
     * 所属用户
     */
    private String uid;

    /**
     * 虚拟设备名称
     */
    private String name;

    /**
     * 产品key
     */
    private String productKey;

    /**
     * 虚拟类型
     */
    private String type;

    /**
     * 设备行为脚本
     */
    @Column(columnDefinition = "text")
    private String script;

    /**
     * 触发方式执行方式
     */
    @Column(name = "[trigger]")
    private String trigger;

    /**
     * 触发表达式
     */
    private String triggerExpression;

    /**
     * 运行状态
     */
    private String state;

    /**
     * 创建时间
     */
    private Long createAt;

}
