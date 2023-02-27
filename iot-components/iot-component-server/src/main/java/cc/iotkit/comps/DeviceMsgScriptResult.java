package cc.iotkit.comps;

import lombok.Data;

import java.util.Map;

@Data
public class DeviceMsgScriptResult {

    private String type;

    private Object data;


    private Map action;

}
