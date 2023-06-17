package cc.iotkit.data.model;

import cc.iotkit.model.ota.OtaPackage;
import io.github.linpeilie.annotations.AutoMapper;
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
@AutoMapper(target = OtaPackage.class)
public class TbOtaPackage {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    private Long id;

    private Long size;

    private String sign;

    private Boolean isDiff;

    private String md5;

    private String name;

    private String desc;

    private String version;

    private String url;

    private String signMethod;

    private String module;

    private String extData;

    private Long createAt;
}
