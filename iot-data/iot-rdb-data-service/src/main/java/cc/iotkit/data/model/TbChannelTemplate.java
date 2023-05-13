package cc.iotkit.data.model;

import lombok.Data;

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

    private String title;

    private String content;

    private Long createAt;
}