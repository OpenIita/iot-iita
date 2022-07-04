/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.model.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 设备配置
 */
@Data
@Document(indexName = "device_config")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceConfig {

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
    private String config;

    @Field(type = FieldType.Date)
    private Long createAt;
}
