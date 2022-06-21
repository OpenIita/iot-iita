/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 用户操作日志
 */
@Document(indexName = "user_action_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActionLog {

    @Id
    private String id;

    private String uid;

    /**
     * 类型
     * 0:设备控制
     * 1:添加设备
     * 2:分享设备
     * 3:创建空间
     * 4:分享家庭
     */
    private int type;

    /**
     * 操作目标
     */
    private String target;

    /**
     * 日志内容
     */
    private Object log;

    /**
     * 操作结果
     */
    private String result;

    @Field(type = FieldType.Date)
    private Long createAt;

    public enum Type {
        DEVICE_CONTROL("设备控制", 0),
        DEVICE_ADD("添加设备", 1),
        DEVICE_SHARED("分享设备", 2),
        SPACE_ADD("创建空间", 3),
        HOME_SHARED("分享家庭", 4);

        private String name;
        private int value;

        private Type(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }
}
