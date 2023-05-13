package cc.iotkit.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 石恒
 * @Date: 2023/5/13 16:04
 * @Description:
 */
@AllArgsConstructor
@Getter
public enum MessageTypeEnum {
    ALERT("alert", "告警");
    private String code;
    private String desc;
}
