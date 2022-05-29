package cc.iotkit.model.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


/**
 * 虚拟设备日志
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "virtual_device_log")
public class VirtualDeviceLog {

    @Id
    private String id;

    /**
     * 虚拟设备id
     */
    private String virtualDeviceId;

    /**
     * 虚拟设备名称
     */
    private String virtualDeviceName;

    /**
     * 关联设备数量
     */
    private int deviceTotal;

    /**
     * 虚拟设备执行结果
     */
    private String result;

    /**
     * 创建时间
     */
    private Long logAt = System.currentTimeMillis();
}
