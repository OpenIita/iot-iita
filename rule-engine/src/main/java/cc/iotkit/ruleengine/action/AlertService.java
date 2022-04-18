package cc.iotkit.ruleengine.action;

import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.ruleengine.alert.Alerter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class AlertService<T extends Alerter> extends ScriptService {
    private String configId;

    private T alert;

    @SneakyThrows
    public String execute(ThingModelMessage msg) {
        //执行转换脚本
        Map result = execScript(msg);
        if (result == null) {
            log.warn("execScript result is null");
            return "execScript result is null";
        }
        return alert.send(result);
    }
}
