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
import cc.iotkit.converter.IConverter;
import lombok.Data;

import java.util.UUID;

@Data
public abstract class AbstractDeviceComponent implements IDeviceComponent {

    protected IMessageHandler handler;

    protected IConverter converter;

    protected CompConfig config;

    protected String script;

    protected String id;

    @Override
    public void create(CompConfig config) {
        this.config = config;
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public void onDeviceRegister(RegisterInfo info) {
    }

    @Override
    public void onDeviceAuth(AuthInfo authInfo) {
    }

    @Override
    public void onDeviceStateChange(DeviceState state) {
    }

    @Override
    public CompConfig getConfig() {
        return config;
    }

    @Override
    public void putScriptEnv(String key, Object value) {
    }
}
