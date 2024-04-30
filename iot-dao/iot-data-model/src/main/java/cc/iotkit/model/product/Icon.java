/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.model.product;

import cc.iotkit.model.Id;
import cc.iotkit.model.TenantModel;
import lombok.*;

import java.io.Serializable;

/**
 * @Author: tfd
 * @Date: 2024/4/25 14:32
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Icon extends TenantModel implements Id<Long>, Serializable {

    private Long id;

    private Long iconTypeId;

    private String iconName;

    private String viewBox;

    private String xmlns;

    private String version;

    private String iconContent;
}