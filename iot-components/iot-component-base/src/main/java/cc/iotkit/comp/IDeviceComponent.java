/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
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

    DeviceMessage send(DeviceMessage message);

    void setHandler(IMessageHandler handler);

    void setConverter(IConverter converter);

    IConverter getConverter();

}
