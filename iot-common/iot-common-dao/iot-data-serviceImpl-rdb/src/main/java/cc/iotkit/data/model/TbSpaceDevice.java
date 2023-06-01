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

import cc.iotkit.model.space.SpaceDevice;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "space_device")
@ApiModel(value = "空间设备")
@AutoMapper(target = SpaceDevice.class)
public class TbSpaceDevice {

    @Id
    private String id;

    /**
     * 关联用户id
     */
    @ApiModelProperty(value = "关联用户id")
    private String uid;

    /**
     * 分享的用户id
     */
    @ApiModelProperty(value = "分享的用户id")
    private String sharedUid;

    /**
     * 空间中的设备id
     */
    @ApiModelProperty(value = "空间中的设备id")
    private String deviceId;

    /**
     * 空间中的设备名称
     */
    @ApiModelProperty(value = "空间中的设备名称")
    private String name;

    /**
     * 所属家庭Id
     */
    @ApiModelProperty(value = "所属家庭Id")
    private String homeId;

    /**
     * 空间id
     */
    @ApiModelProperty(value = "空间id")
    private String spaceId;

    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    private Long addAt;

    /**
     * 使用时间
     */
    @ApiModelProperty(value = "使用时间")
    private Long useAt;

    /**
     * 是否收藏
     */
    @ApiModelProperty(value = "是否收藏")
    private Boolean collect;

}
