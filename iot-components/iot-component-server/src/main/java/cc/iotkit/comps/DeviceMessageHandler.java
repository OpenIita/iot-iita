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

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.comp.IDeviceComponent;
import cc.iotkit.comp.IMessageHandler;
import cc.iotkit.comp.model.AuthInfo;
import cc.iotkit.comp.model.ReceiveResult;
import cc.iotkit.comp.model.RegisterInfo;
import cc.iotkit.converter.DeviceMessage;
import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.comps.service.DeviceBehaviourService;
import cc.iotkit.converter.IConverter;
import cc.iotkit.model.device.message.ThingModelMessage;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Slf4j
@Data
public class DeviceMessageHandler implements IMessageHandler {
    private final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager()).getEngineByName("nashorn");

    private final Object scriptObj;

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
                                String script, IConverter converter,
                                DeviceBehaviourService deviceBehaviourService,
                                DeviceRouter deviceRouter
    ) {
        this.deviceComponentManager = deviceComponentManager;
        this.component = component;
        this.converter = converter;
        this.deviceBehaviourService = deviceBehaviourService;
        this.deviceRouter = deviceRouter;

        engine.put("component", component);
        scriptObj = engine.eval(String.format("new (function () {\n%s})()", script));
    }

    public void onReceive(Map<String, Object> head, String type, String msg) {
        onReceive(head, type, msg, (r) -> {
        });
    }

    public void onReceive(Map<String, Object> head, String type, String msg, Consumer<ReceiveResult> onResult) {
        executorService.submit(() -> {
            try {
                ScriptObjectMirror result = (ScriptObjectMirror) invokeMethod("onReceive", head, type, msg);
                log.info("onReceive script result:{}", JsonUtil.toJsonString(result));
                Object rstType = result.get("type");
                if (rstType == null) {
                    onResult.accept(null);
                    return;
                }
                //取脚本执行后返回的数据
                Object data = JsonUtil.toObject((ScriptObjectMirror) result.get("data"));
                if (!(data instanceof Map)) {
                    throw new BizException("script result data is incorrect");
                }

                Map<String, Object> dataMap = (Map) data;
                //获取动作数据
                Action action = getAction(result.get("action"));

                if ("register".equals(rstType)) {
                    //注册数据
                    RegisterInfo regInfo = RegisterInfo.from(dataMap);
                    if (regInfo == null) {
                        onResult.accept(null);
                        return;
                    }
                    doRegister(regInfo);
                    doAction(action);
                    onResult.accept(new ReceiveResult(regInfo.getProductKey(), regInfo.getDeviceName(), regInfo));
                    return;
                } else if ("auth".equals(rstType)) {
                    //设备认证
                    AuthInfo authInfo = new AuthInfo();
                    BeanUtils.populate(authInfo, dataMap);
                    doAuth(authInfo);
                    doAction(action);
                    onResult.accept(new ReceiveResult(authInfo.getProductKey(), authInfo.getDeviceName(), authInfo));
                    return;
                } else if ("state".equals(rstType)) {
                    //设备状态变更
                    DeviceState state = DeviceState.from(dataMap);
                    if (state == null) {
                        onResult.accept(null);
                        return;
                    }
                    doStateChange(state);
                    doAction(action);
                    onResult.accept(new ReceiveResult(state.getProductKey(), state.getDeviceName(), state));
                    return;
                } else if ("report".equals(rstType)) {
                    //上报数据
                    DeviceMessage message = new DeviceMessage();
                    BeanUtils.populate(message, dataMap);
                    doReport(message);
                    doAction(action);
                    onResult.accept(new ReceiveResult(message.getProductKey(), message.getDeviceName(), message));
                    return;
                }

            } catch (Throwable e) {
                log.error("receive component message error", e);
            }
            onResult.accept(null);
        });
    }

    private void doRegister(RegisterInfo reg) throws ScriptException, NoSuchMethodException {
        try {
            deviceBehaviourService.register(reg);
        } catch (Throwable e) {
            log.error("register error", e);
        } finally {
            invokeMethod("onRegistered", reg, "false");
        }
    }

    private void doAuth(AuthInfo auth) throws ScriptException, NoSuchMethodException {
        try {
            deviceBehaviourService.deviceAuth(auth.getProductKey(),
                    auth.getDeviceName(),
                    auth.getProductSecret(),
                    auth.getDeviceSecret());
        } catch (Throwable e) {
            log.error("device auth error", e);
        } finally {
            invokeMethod("onAuthed", auth, "false");
        }
    }

    private Object invokeMethod(String name, Object... args) throws ScriptException, NoSuchMethodException {
        if (((ScriptObjectMirror) scriptObj).get(name) != null) {
            return engine.invokeMethod(scriptObj, name, args);
        }
        return null;
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

    private Action getAction(Object objAction) {
        if (!(objAction instanceof Map)) {
            return null;
        }
        Action action = new Action();
        try {
            BeanUtils.populate(action, (Map<String, ? extends Object>) objAction);
        } catch (Throwable e) {
            log.error("parse action error", e);
        }
        return action;
    }

    private void doAction(Action action) {
        if (action == null) {
            return;
        }
        try {
            if (Action.TYPE_ACK.equals(action.getType())) {
                DeviceMessage deviceMessage = JsonUtil.parse(action.getContent(), DeviceMessage.class);
                this.getComponent().send(deviceMessage);
            }
        } catch (Throwable e) {
            log.error("do action error", e);
        }
    }

    @Override
    public void putScriptEnv(String key, Object value) {
        engine.put(key, value);
    }

    @Data
    public static class Action {
        public static final String TYPE_ACK = "ack";

        private String type;
        private String content;
    }

}
