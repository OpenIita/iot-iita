package cc.iotkit.data.model;

import cc.iotkit.model.system.SysNotice;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 通知公告表 sys_notice
 *
 * @author Lion Li
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_notice")
@AutoMapper(target = SysNotice.class)
@ApiModel(value = "通知公告表")
public class TbSysNotice extends BaseEntity {

    /**
     * 公告ID
     */
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "公告ID")
    private Long id;

    /**
     * 租户编号
     */
    @ApiModelProperty(value = "租户编号")
    private String tenantId;

    /**
     * 公告标题
     */
    @ApiModelProperty(value = "公告标题")
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    @ApiModelProperty(value = "公告类型（1通知 2公告）")
    private String noticeType;

    /**
     * 公告内容
     */
    @ApiModelProperty(value = "公告内容")
    private String noticeContent;

    /**
     * 公告状态（0正常 1关闭）
     */
    @ApiModelProperty(value = "公告状态（0正常 1关闭）")
    private String status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
