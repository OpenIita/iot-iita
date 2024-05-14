/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.model.modbus;

import cc.iotkit.model.Owned;
import cc.iotkit.model.TenantModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: modbus信息
 * @Author: ZOUZDC
 * @Date: 2024/4/28 22:49
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModbusInfo extends TenantModel implements Owned<Long>, Serializable {

    private Long id;

    /**
     * 配置所属用户
     */
    private String uid;

    /**
     * 产品名称
     */
    private String name;

    private String productKey;

    /**
     * 说明
     */
    private String remark;

    /**
     * 创建时间
     */
    private Long createAt;

    /**
     * 更新时间
     */
    private Long updateAt;



}
