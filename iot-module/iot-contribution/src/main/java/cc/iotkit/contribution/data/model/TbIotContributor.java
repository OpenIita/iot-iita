package cc.iotkit.contribution.data.model;

import cc.iotkit.contribution.model.IotContributor;
import cc.iotkit.data.model.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 贡献者对象 iot_contributor
 *
 * @author Lion Li
 * @date 2023-07-04
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "iot_contributor")
@AutoMapper(target = IotContributor.class)
public class TbIotContributor extends BaseEntity {



    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 贡献者名称
     */
    @ApiModelProperty(value = "贡献者名称")
    private String contributor;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;

    /**
     * 岗位(1前端开发,2后端开发,3全栈开发,4测试,5ui设计师,6产品经理,7架构师)
     */
    @ApiModelProperty(value = "岗位(1前端开发,2后端开发,3全栈开发,4测试,5ui设计师,6产品经理,7架构师)")
    private Integer post;

    /**
     * 简介
     */
    @ApiModelProperty(value = "简介")
    private String intro;

    /**
     * tag列表(为了简单,逗号隔开)
     */
    @ApiModelProperty(value = "tag列表(为了简单,逗号隔开)")
    private String tags;

    /**
     * 详情标题
     */
    @ApiModelProperty(value = "详情标题")
    private String title;

    /**
     * 详情
     */
    @ApiModelProperty(value = "详情")
    private String context;

    /**
     * 帐号状态（0正常 1停用）
     */
    @ApiModelProperty(value = "帐号状态（0正常 1停用）")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;


}
