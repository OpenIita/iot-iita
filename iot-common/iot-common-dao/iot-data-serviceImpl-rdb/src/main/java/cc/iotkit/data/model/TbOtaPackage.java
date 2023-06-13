package cc.iotkit.data.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 石恒
 * @Date: 2023/5/19 21:25
 * @Description:
 */
@Data
@Entity
@Table(name = "ota_package")
public class TbOtaPackage {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    private String id;

    private Long size;

    private String sign;

    private Boolean isDiff;

    private String md5;

    private String name;

    private String desc;

    private String version;

    private String url;

    private Long createAt;
}
