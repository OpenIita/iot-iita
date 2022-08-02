package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "device_config")
public class TbDeviceConfig {

    @Id
    private String id;

    private String deviceId;

    /**
     * 产品key
     */
    private String productKey;

    private String deviceName;

    /**
     * 设备配置json内容
     */
    @Column(columnDefinition = "text")
    private String config;

    private Long createAt;

}
