package cc.iotkit.data.model;

import cc.iotkit.data.model.BaseEntity;
import cc.iotkit.model.system.SysLogininfor;
import cc.iotkit.model.system.SysNotice;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
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
public class TbSysNotice extends BaseEntity {

    /**
     * 公告ID
     */
    @Id
    private Long noticeId;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    private String noticeType;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 公告状态（0正常 1关闭）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
