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
@Table(name = "protocol_converter")
public class TbProtocolConverter {

    @Id
    private String id;

    /**
     * 所属性用户id
     */
    private String uid;

    private String name;

    @Column(name = "[desc]")
    private String desc;

    private Long createAt;


    private String typ;

    // 脚本内容
    private String script;

}
