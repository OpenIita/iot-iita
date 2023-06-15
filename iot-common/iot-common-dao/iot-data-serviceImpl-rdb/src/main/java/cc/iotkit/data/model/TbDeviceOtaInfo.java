package cc.iotkit.data.model;

import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 石恒
 * @Date: 2023/6/15 22:22
 * @Description:
 */
@Data
@Entity
@Table(name = "device_ota_info")
@ApiModel(value = "设备信息")
@AutoMapper(target = TbDeviceOtaInfo.class)
public class TbDeviceOtaInfo {
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    private String id;
}
