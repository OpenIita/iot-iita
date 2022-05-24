package cc.iotkit.comp;

import cc.iotkit.comp.model.AuthInfo;
import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.comp.model.RegisterInfo;
import cc.iotkit.converter.DeviceMessage;
import cc.iotkit.converter.IConverter;

public interface IDeviceComponent extends IComponent {

    void onDeviceAuth(AuthInfo authInfo);

    void onDeviceRegister(RegisterInfo info);

    void onDeviceStateChange(DeviceState state);

    void send(DeviceMessage message);

    void setHandler(IMessageHandler handler);

    void setConverter(IConverter converter);

    IConverter getConverter();

}
