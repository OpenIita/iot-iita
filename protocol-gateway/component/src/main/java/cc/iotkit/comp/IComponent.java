package cc.iotkit.comp;

import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.comp.model.RegisterInfo;
import cc.iotkit.converter.DeviceMessage;
import cc.iotkit.converter.IConverter;

public interface IComponent {

    void create(CompConfig config);

    void start();

    void stop();

    void destroy();

    void onDeviceStateChange(DeviceState state);

    void send(DeviceMessage message);

    boolean exist(String productKey, String deviceName);

    void setHandler(IMessageHandler handler);

    void setConverter(IConverter converter);

    IConverter getConverter();

    CompConfig getConfig();
}
