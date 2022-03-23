package cc.iotkit.comps;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.comp.IMessageHandler;
import cc.iotkit.comp.model.DeviceMessage;
import cc.iotkit.comps.model.AuthInfo;
import cc.iotkit.comps.model.DeviceState;
import cc.iotkit.comps.model.RegisterInfo;
import cc.iotkit.comps.service.DeviceBehaviourService;
import cc.iotkit.converter.IConverter;
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

    @SneakyThrows
    public MessageHandler(String script, IConverter converter,
                          DeviceBehaviourService deviceBehaviourService) {
        this.converter = converter;
        this.deviceBehaviourService = deviceBehaviourService;
        scriptObj = engine.eval(script);
    }

    public void register(Map<String, Object> head, String msg) {
    }

    public void auth(Map<String, Object> head, String msg) {
    }

    public void state(Map<String, Object> head, String msg) {
    }

    public void onReceive(Map<String, Object> head, String type, String msg) {
        try {
            ScriptObjectMirror result = (ScriptObjectMirror) engine.invokeMethod(scriptObj, "onReceive", head, type, msg);
            Object rstType = result.get("type");
            if (rstType == null) {
                return;
            }
            //取脚本执行后返回的数据
            Object data = result.get("data");
            if (!(data instanceof Map)) {
                throw new BizException("script result data is incorrect");
            }
            Map<String, Object> dataMap = (Map) data;

            if ("register".equals(rstType)) {
                //注册数据
                RegisterInfo regInfo = new RegisterInfo();
                BeanUtils.populate(regInfo, dataMap);
                doRegister(regInfo);
            } else if ("auth".equals(rstType)) {
                //设备认证
                AuthInfo authInfo = new AuthInfo();
                BeanUtils.populate(authInfo, dataMap);
                doAuth(authInfo);
            } else if ("state".equals(rstType)) {
                //设备状态变更
                DeviceState state = new DeviceState();
                BeanUtils.populate(state, dataMap);
                doStateChange(state);
            } else if ("report".equals(rstType)) {
                //上报数据
                DeviceMessage message = new DeviceMessage();
                BeanUtils.populate(message, dataMap);
                doReport(message);
            }

        } catch (BizException e) {
            throw e;
        } catch (Throwable e) {
            throw new BizException("receive component message error", e);
        }
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
            deviceBehaviourService.deviceStateChange(state.getProductKey(),
                    state.getDeviceName(),
                    DeviceState.STATE_ONLINE.equals(state.getState()));
        } catch (Throwable e) {
            log.error("device state change error", e);
        }
    }

    private void doReport(DeviceMessage message) {
        try {
            deviceBehaviourService.reportMessage(message);
        } catch (Throwable e) {
            log.error("report device message error", e);
        }
    }
}
