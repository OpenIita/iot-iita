package cc.iotkit.contribution.dto.bo;

import cc.iotkit.contribution.model.IotContributor;
import cc.iotkit.common.api.BaseDto;

import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 贡献者业务对象 iot_contributor
 *
 * @author Lion Li
 * @date 2023-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = IotContributor.class, reverseConvertGenerate = false)
public class IotContributorBo extends BaseDto {

    /**
     * 
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    @ApiModelProperty(value = "", required = true)
    private Long id;

    /**
     * 贡献者名称
     */
    @NotBlank(message = "贡献者名称不能为空", groups = { AddGroup.class, EditGroup.class })
    @ApiModelProperty(value = "贡献者名称", required = true)
    private String contributor;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像", required = false)
    private String avatar;

    /**
     * 岗位(1前端开发,2后端开发,3全栈开发,4测试,5ui设计师,6产品经理,7架构师)
     */
    @ApiModelProperty(value = "岗位(1前端开发,2后端开发,3全栈开发,4测试,5ui设计师,6产品经理,7架构师)", required = false)
    private Long post;

    /**
     * 简介
     */
    @ApiModelProperty(value = "简介", required = false)
    private String intro;

    /**
     * tag列表(为了简单,逗号隔开)
     */
    @ApiModelProperty(value = "tag列表(为了简单,逗号隔开)", required = false)
    private String tags;

    /**
     * 详情标题
     */
    @ApiModelProperty(value = "详情标题", required = false)
    private String title;

    /**
     * 详情
     */
    @ApiModelProperty(value = "详情", required = false)
    private String context;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    @ApiModelProperty(value = "排序", required = true)
    private Long score;

    /**
     * 帐号状态（0正常 1停用）
     */
    @NotBlank(message = "帐号状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    @ApiModelProperty(value = "帐号状态（0正常 1停用）", required = true)
    private String status;


}
