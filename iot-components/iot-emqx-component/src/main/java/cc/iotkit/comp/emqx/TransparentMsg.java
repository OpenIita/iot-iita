package cc.iotkit.comp.emqx;

import lombok.Data;

@Data
public class TransparentMsg {

    private String productKey;

    /**
     * 生成给设备端的消息id
     */
    private String mid;

    private String model;

    private String mac;

    private String data;

}
