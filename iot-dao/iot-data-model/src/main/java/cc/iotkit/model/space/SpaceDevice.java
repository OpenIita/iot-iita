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
public class SpaceDevice implements Owned<String> {

    private String id;

    /**
     * 关联用户id
     */
    private String uid;

    /**
     * 分享的用户id
     */
    private String sharedUid;

    /**
     * 空间中的设备id
     */
    private String deviceId;

    /**
     * 空间中的设备名称
     */
    private String name;

    /**
     * 所属家庭Id
     */
    private String homeId;

    /**
     * 空间id
     */
    private String spaceId;

    /**
     * 添加时间
     */
    private Long addAt;

    /**
     * 使用时间
     */
    private Long useAt;

    /**
     * 是否收藏
     */
    private Boolean collect;

}
