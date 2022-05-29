package cc.iotkit.comp.emqx;

import cc.iotkit.common.thing.ThingService;
import cc.iotkit.model.device.message.ThingModelMessage;

public class JsScripter implements IScripter {

    @Override
    public void setScript(String script) {
    }

    public ThingModelMessage decode(TransparentMsg msg) {
        return null;
    }

    public TransparentMsg encode(ThingService<?> service) {
        return null;
    }
}
