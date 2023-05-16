package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 石恒
 * @Date: 2023/5/11 20:59
 * @Description:
 */
@Data
@Entity
@Table(name = "channel_template")
public class TbChannelTemplate {
    @Id
    private String id;

    private String channelConfigId;

    private String channelCode;

    private String title;

    @Column(columnDefinition ="TEXT")
    private String content;

    private Long createAt;
}
