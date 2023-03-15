package cc.iotkit.comp.websocket;

import cc.iotkit.comp.IMessageHandler;
import cc.iotkit.converter.DeviceMessage;
import io.vertx.core.AbstractVerticle;
import lombok.Data;

@Data
public abstract class AbstractDeviceVerticle extends AbstractVerticle {

    public static final String TYPE_SERVER = "server";
    public static final String TYPE_CLIENT = "client";

    protected IMessageHandler executor;

    public abstract DeviceMessage send(DeviceMessage message);

}
