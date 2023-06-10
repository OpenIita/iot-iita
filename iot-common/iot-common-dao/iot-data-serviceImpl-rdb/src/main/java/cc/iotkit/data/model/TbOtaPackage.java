package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Entity;
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
    private String id;

    private Long size;

    private String sign;

    private String name;

    private String desc;

    private String version;

    private String url;

    private Long createAt;
}
