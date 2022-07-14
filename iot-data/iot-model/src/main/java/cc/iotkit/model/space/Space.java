/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.model.space;

import cc.iotkit.model.Owned;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Space implements Owned<String> {

    private String id;

    /**
     * 关联家庭id
     */
    private String homeId;

    /**
     * 关联用户id
     */
    private String uid;

    /**
     * 空间名称
     */
    private String name;

    /**
     * 设备数量
     */
    private Integer deviceNum;

    private Long createAt;
}
