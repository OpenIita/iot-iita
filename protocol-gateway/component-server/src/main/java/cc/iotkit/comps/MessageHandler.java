package cc.iotkit.comps;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.comp.IComponent;
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

@Slf4j
@Data
public class MessageHandler implements IMessageHandler {
    private final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager()).getEngineByName("nashorn");

    private final Object scriptObj;

    private final IConverter converter;

    private final DeviceBehaviourService deviceBehaviourService;

    private final ComponentManager componentManager;

    private final IComponent component;

    @SneakyThrows
    public MessageHandler(ComponentManager componentManager,
                          IComponent component,
                          String script, IConverter converter,
                          DeviceBehaviourService deviceBehaviourService) {
        this.componentManager = componentManager;
        this.component = component;
        this.converter = converter;
        this.deviceBehaviourService = deviceBehaviourService;
        scriptObj = engine.eval(script);
    }

    public ReceiveResult onReceive(Map<String, Object> head, String type, String msg) {
        try {
            ScriptObjectMirror result = (ScriptObjectMirror) engine.invokeMethod(scriptObj, "onReceive", head, type, msg);
            log.info("onReceive script result:{}", JsonUtil.toJsonString(result));
            Object rstType = result.get("type");
            if (rstType == null) {
                return null;
            }
            //取脚本执行后返回的数据
            Object data = JsonUtil.toObject((ScriptObjectMirror) result.get("data"));
            if (!(data instanceof Map)) {
                throw new BizException("script result data is incorrect");
            }
            Map<String, Object> dataMap = (Map) data;

            if ("register".equals(rstType)) {
                //注册数据
                RegisterInfo regInfo = RegisterInfo.from(dataMap);
                if (regInfo == null) {
                    return null;
                }
                doRegister(regInfo);
                return new ReceiveResult(regInfo.getProductKey(), regInfo.getDeviceName(), regInfo);
            } else if ("auth".equals(rstType)) {
                //设备认证
                AuthInfo authInfo = new AuthInfo();
                BeanUtils.populate(authInfo, dataMap);
                doAuth(authInfo);
                return new ReceiveResult(authInfo.getProductKey(), authInfo.getDeviceName(), authInfo);
            } else if ("state".equals(rstType)) {
                //设备状态变更
                DeviceState state = DeviceState.from(dataMap);
                if (state == null) {
                    return null;
                }
                doStateChange(state);
                return new ReceiveResult(state.getProductKey(), state.getDeviceName(), state);
            } else if ("report".equals(rstType)) {
                //上报数据
                DeviceMessage message = new DeviceMessage();
                BeanUtils.populate(message, dataMap);
                doReport(message);
                return new ReceiveResult(message.getProductKey(), message.getDeviceName(), message);
            }

        } catch (BizException e) {
            throw e;
        } catch (Throwable e) {
            throw new BizException("receive component message error", e);
        }
        return null;
    }

    private void doRegister(RegisterInfo reg) throws ScriptException, NoSuchMethodException {
        try {
            deviceBehaviourService.register(reg);
            engine.invokeMethod(scriptObj, "onRegistered", reg, "true");
        } catch (Throwable e) {
            log.error("register error", e);
            engine.invokeMethod(scriptObj, "onRegistered", reg, "false");
        }
    }

    private void doAuth(AuthInfo auth) throws ScriptException, NoSuchMethodException {
        try {
            deviceBehaviourService.deviceAuth(auth.getProductKey(),
                    auth.getDeviceName(),
                    auth.getProductSecret(),
                    auth.getDeviceSecret());
            engine.invokeMethod(scriptObj, "onAuthed", auth, true);
        } catch (Throwable e) {
            log.error("device auth error", e);
            engine.invokeMethod(scriptObj, "onAuthed", auth, false);
        }
    }

    private void doStateChange(DeviceState state) {
        try {
            component.onDeviceStateChange(state);
            deviceBehaviourService.deviceStateChange(state.getProductKey(),
                    state.getDeviceName(),
                    DeviceState.STATE_ONLINE.equals(state.getState()));
        } catch (Throwable e) {
            log.error("device state change error", e);
        }
    }

    private void doReport(DeviceMessage message) {
        ThingModelMessage thingModelMessage = converter.decode(message);

        //服务回复需要重新对应mid
        if (thingModelMessage.getIdentifier().endsWith("_reply")) {
            String platformMid = componentManager.getPlatformMid(message.getDeviceName(), message.getMid());
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
}
