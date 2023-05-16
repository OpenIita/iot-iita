package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 石恒
 * @Date: 2023/5/11 20:58
 * @Description:
 */
@Data
@Entity
@Table(name = "channel_config")
public class TbChannelConfig {
    @Id
    private String id;

    private String channelId;

    private String title;

    @Column(columnDefinition ="TEXT")
    private String param;

    private Long createAt;
}
