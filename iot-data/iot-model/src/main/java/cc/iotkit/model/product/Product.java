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

import cc.iotkit.model.Owned;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Owned<String> {

    //定位更新方式，手动/设备上报
    public static final String LOCATE_MANUAL = "manual";
    public static final String LOCATE_DEVICE = "device";

    private String id;

    private String name;

    private String category;

    private Integer nodeType;

    /**
     * 所属平台用户ID
     */
    private String uid;

    private String img;

    /**
     * 是否透传
     */
    private Boolean transparent;

    /**
     * 是否开启设备定位,true/false
     */
        private Boolean isOpenLocate;

    /**
     * 定位更新方式
     */
    private String locateUpdateType;

    private Long createAt;

    public boolean isTransparent() {
        return transparent != null && transparent;
    }
}