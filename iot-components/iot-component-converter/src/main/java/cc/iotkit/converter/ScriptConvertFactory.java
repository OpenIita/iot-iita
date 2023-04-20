package cc.iotkit.converter;

import cc.iotkit.common.thing.ThingService;
import cc.iotkit.model.device.message.ThingModelMessage;

public class ScriptConvertFactory {

    public static IConverter getCovert(String type) {
        if (type == null) {
            type = "js";
        }
        switch (type) {
            case "python":
            case "lua":
                return new IConverter() {
                    @Override
                    public void setScript(String script) {

                    }

                    @Override
                    public ThingModelMessage decode(DeviceMessage msg) {
                        return null;
                    }

                    @Override
                    public DeviceMessage encode(ThingService<?> service, Device device) {
                        return null;
                    }

                    @Override
                    public void putScriptEnv(String key, Object value) {

                    }
                };
            case "js":
            default:
                return new JavaScriptConverter();
        }
    }
}
