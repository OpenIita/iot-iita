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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "space_device")
public class TbSpaceDevice {

    @Id
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
