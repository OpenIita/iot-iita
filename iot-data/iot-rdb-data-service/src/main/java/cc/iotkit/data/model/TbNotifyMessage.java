package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 石恒
 * @Date: 2023/5/13 18:33
 * @Description:
 */
@Data
@Entity
@Table(name = "notify_message")
public class TbNotifyMessage {
    @Id
    private String id;

    private String content;

    private String messageType;

    private Boolean status;

    private Long createAt;
}
