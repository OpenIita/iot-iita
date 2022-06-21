/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.model.alert;

import cc.iotkit.model.Owned;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 告警配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "alert_config")
public class AlertConfig implements Owned {
    public static final String TYPE_EMAIL="email";
    public static final String TYPE_DINGDING_ROBOT="dingding_robot";

    @Id
    private String id;

    /**
     * 配置所属用户
     */
    private String uid;

    /**
     * 告警器类型
     */
    private String type;

    /**
     * 告警配置标题
     */
    private String title;

    /**
     * 告警器参数配置
     */
    private String config;

    /**
     * 告警内容模板
     */
    private String template;

    /**
     * 是否启用
     */
    private boolean enable;

    @Field(type = FieldType.Date)
    private Long createAt;

}
