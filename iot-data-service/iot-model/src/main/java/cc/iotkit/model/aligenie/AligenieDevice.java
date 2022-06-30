/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.model.aligenie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "aligenie_device")
public class AligenieDevice {
    @Id
    private String id;

    /**
     * 关联用户id
     */
    private String uid;

    /**
     * 用户token
     */
    private String token;

    /**
     * 空间中的设备id
     */
    private String deviceId;

    /**
     * 天猫精灵产品ID
     */
    private String productId;

    /**
     * 空间中的设备名称
     */
    private String name;

    /**
     * 空间名称
     */
    private String spaceName;

    /**
     * 设备当前属性值
     */
    private Map<String, Object> status;
}
