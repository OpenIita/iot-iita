/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comps;

import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.comp.IDeviceComponent;
import cc.iotkit.comp.IMessageHandler;
import cc.iotkit.comp.model.AuthInfo;
import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.comp.model.ReceiveResult;
import cc.iotkit.comp.model.RegisterInfo;
import cc.iotkit.comps.service.DeviceBehaviourService;
import cc.iotkit.converter.DeviceMessage;
import cc.iotkit.converter.IConverter;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.script.IScriptEngine;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
@Getter
@Setter
public class DeviceMessageHandler implements IMessageHandler {


    private final IScriptEngine scriptEngine;

    private final IConverter converter;

    private final DeviceBehaviourService deviceBehaviourService;

    private final DeviceComponentManager deviceComponentManager;

    private final IDeviceComponent component;

    private final DeviceRouter deviceRouter;

    private final ExecutorService executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());

    @SneakyThrows
    public DeviceMessageHandler(DeviceComponentManager deviceComponentManager,
                                IDeviceComponent component,
                                IScriptEngine scriptEngine,
                                String script, IConverter converter,
                                DeviceBehaviourService deviceBehaviourService,
                                DeviceRouter deviceRouter
    ) {
        this.deviceComponentManager = deviceComponentManager;
        this.component = component;
        this.converter = converter;
        this.deviceBehaviourService = deviceBehaviourService;
        this.deviceRouter = deviceRouter;
        this.scriptEngine = scriptEngine;
        scriptEngine.putScriptEnv("component", component);
        scriptEngine.setScript(script);
    }

    @Override
    public void onReceive(Map<String, Object> head, String type, String msg) {
        onReceive(head, type, msg, (r) -> {
        });
    }

    @Override
    public void onReceive(Map<String, Object> head, String type, String msg, Consumer<ReceiveResult> onResult) {
        executorService.submit(() -> {
            try {
                Map<String, Object> rst = scriptEngine.invokeMethod(new TypeReference<>() {
                }, "onReceive", head, type, msg);
                Object objType = rst.get("type");
                if (objType == null) {
                    onResult.accept(null);
                    return;
                }
                //取脚本执行后返回的数据
                Object objData = rst.get("data");
                if (!(objData instanceof Map)) {
                    onResult.accept(null);
                    return;
                }
                Map data = (Map) objData;

                //获取动作数据
                Action action = MessageParser.parse(new Action(), rst.get("action"));

                switch (objType.toString()) {
                    case "register":
                        //注册数据
                        RegisterInfo regInfo = MessageParser.parseRegisterInfo(data);
                        if (regInfo == null) {
                            onResult.accept(null);
                            return;
                        }
                        doRegister(regInfo);
                        doAction(action);
                        onResult.accept(new ReceiveResult(regInfo.getProductKey(), regInfo.getDeviceName(), regInfo));
                        return;
                    case "auth":
                        //设备认证
                        AuthInfo authInfo = MessageParser.parse(new AuthInfo(), data);
                        doAuth(authInfo);
                        doAction(action);
                        onResult.accept(new ReceiveResult(authInfo.getProductKey(), authInfo.getDeviceName(), authInfo));
                        return;
                    case "state":
                        //设备状态变更
                        DeviceState state = MessageParser.parseDeviceState(data);
                        doStateChange(state);
                        doAction(action);
                        onResult.accept(new ReceiveResult(state.getProductKey(), state.getDeviceName(), state));
                        return;
                    case "report":
                        //上报数据
                        DeviceMessage message = MessageParser.parse(new DeviceMessage(), data);
                        doReport(message);
                        doAction(action);
                        onResult.accept(new ReceiveResult(message.getProductKey(), message.getDeviceName(), message));
                        return;
                    case "ota":
                        //上报数据
                        DeviceMessage otaMessage = MessageParser.parse(new DeviceMessage(), data);
                        doOta(otaMessage);
                        doAction(action);
                        onResult.accept(new ReceiveResult(otaMessage.getProductKey(), otaMessage.getDeviceName(), otaMessage));
                        return;
                }

            } catch (Throwable e) {
                log.error("receive component message error", e);
            }
            onResult.accept(null);
        });
    }

    private void doRegister(RegisterInfo reg) {
        try {
            deviceBehaviourService.register(reg);
        } catch (Throwable e) {
            log.error("register error", e);
        } finally {
            scriptEngine.invokeMethod("onRegistered", reg, "false");
        }
    }

    private void doAuth(AuthInfo auth) {
        try {
            deviceBehaviourService.deviceAuth(auth.getProductKey(),
                    auth.getDeviceName(),
                    auth.getProductSecret(),
                    auth.getDeviceSecret());
        } catch (Throwable e) {
            log.error("device auth error", e);
        } finally {
            scriptEngine.invokeMethod("onAuthed", auth, "false");
        }
    }

    private void doStateChange(DeviceState state) {
        try {
            String pk = state.getProductKey();
            String dn = state.getDeviceName();
            boolean isOnline = DeviceState.STATE_ONLINE.equals(state.getState());
            if (isOnline) {
                deviceRouter.putRouter(pk, dn, component);
            } else {
                deviceRouter.removeRouter(pk, dn);
            }
            component.onDeviceStateChange(state);
            deviceBehaviourService.deviceStateChange(pk, dn, isOnline);
        } catch (Throwable e) {
            log.error("device state change error", e);
        }
    }

    private void doOta(DeviceMessage message) {
        ThingModelMessage thingModelMessage = converter.decode(message);
        deviceBehaviourService.deviceOta(thingModelMessage);
    }

    private void doReport(DeviceMessage message) {
        ThingModelMessage thingModelMessage = converter.decode(message);

        //服务回复需要重新对应mid
        if (thingModelMessage.getIdentifier().endsWith("_reply")) {
            String platformMid = deviceComponentManager.getPlatformMid(thingModelMessage.getDeviceName(), message.getMid());
            if (platformMid == null) {
                platformMid = UniqueIdUtil.newRequestId();
            }
            thingModelMessage.setMid(platformMid);
        } else {
            //其它消息重新生成唯一MID
            thingModelMessage.setMid(UniqueIdUtil.newRequestId());
        }

        deviceBehaviourService.reportMessage(thingModelMessage);
    }

    private void doAction(Action action) {
        if (action == null) {
            return;
        }
        try {
            if (Action.TYPE_ACK.equals(action.getType())) {
                DeviceMessage deviceMessage = JsonUtils.parseObject(action.getContent(), DeviceMessage.class);
                this.getComponent().send(deviceMessage);
            }
        } catch (Throwable e) {
            log.error("do action error", e);
        }
    }

    @Override
    public void putScriptEnv(String key, Object value) {

        scriptEngine.putScriptEnv(key, value);
    }

    @Data
    public static class Action {
        public static final String TYPE_ACK = "ack";

        private String type;
        private String content;
    }

}
