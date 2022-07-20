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
@Table(name = "rule_info")
public class TbRuleInfo {

    @Id
    private String id;

    private String name;

    private String type;

    @Column(columnDefinition = "text")
    private String listeners;

    @Column(columnDefinition = "text")
    private String filters;

    @Column(columnDefinition = "text")
    private String actions;

    private String uid;

    private String state;

    @Column(name = "[desc]")
    private String desc;

    private Long createAt;

}
