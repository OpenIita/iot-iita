package cc.iotkit.data.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "alert_record")
public class TbAlertRecord {

    @Id
    private String id;

    /**
     * 配置所属用户
     */
    private String uid;

    /**
     * 告警名称
     */
    private String name;

    /**
     * 告警严重度（1-5）
     */
    private String level;

    /**
     * 告警时间
     */
    private Long alartTime;

    /**
     * 告警详情
     */
    private String details;

    /**
     * 是否已读
     */
    private Boolean read;

}
