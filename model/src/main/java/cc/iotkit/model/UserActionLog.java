package cc.iotkit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 用户操作日志
 */
@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActionLog {

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
