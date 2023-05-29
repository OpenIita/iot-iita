package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * author: 石恒
 * date: 2023-05-11 17:53
 * description:
 **/
@Data
@Entity
@Table(name = "channel")
public class TbChannel {
    @Id
    private String id;

    private String code;

    private String title;

    private String icon;

    private Long createAt;
}
