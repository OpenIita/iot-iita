package cc.iotkit.converter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 物模型消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThingModelMessage {

    private String productKey;

    private String deviceName;

    private String mid;

    private String identifier;

    private Map<String, Object> data;

    /**
     * 时间戳，设备上的事件或数据产生的本地时间
     */
    private Long occur;

    /**
     * 消息上报时间
     */
    private Long time;

    public static ThingModelMessage from(Map<?,?> map) {
        ThingModelMessage message = new ThingModelMessage();
        message.setProductKey(getStr(map, "productKey"));
        message.setDeviceName(getStr(map, "deviceName"));
        message.setMid(getStr(map, "mid"));
        message.setIdentifier(getStr(map, "identifier"));
        Object data = map.get("data");
        if (data instanceof Map) {
            message.setData((Map<String, Object>) data);
        } else {
            message.setData(new HashMap<>());
        }
        return message;
    }

    private static String getStr(Map<?,?> map, String key) {
        Object val = map.get(key);
        if (val == null) {
            return null;
        }
        return val.toString();
    }
}
