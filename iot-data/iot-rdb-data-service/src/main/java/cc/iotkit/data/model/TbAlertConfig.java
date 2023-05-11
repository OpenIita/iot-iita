package cc.iotkit.data.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "alert_config")
public class TbAlertConfig {

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
     * 告警严重度
     */
    private String level;

    /**
     * 关联规则引擎ID
     */
    private String ruleInfoId;

    /**
     * 关联消息转发模板ID
     */
    private String messageTemplateId;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 创建时间
     */
    private Long createAt;

}
