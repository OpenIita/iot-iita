package cc.iotkit.protocol;

import lombok.Data;

import java.util.Map;

/**
 * 注册信息
 */
@Data
public class RegisterInfo {

    private String productKey;

    private String deviceName;

    private Map<String,Object> label;
}
