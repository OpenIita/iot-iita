package cc.iotkit.ruleengine.alert;

import java.util.Map;

/**
 * 钉钉机器人告警器
 */
public class DingdRobotAlerter implements Alerter {
    @Override
    public void setConfig(String config) {

    }

    @Override
    public void setTemplate(String template) {

    }

    @Override
    public String send(Map<String, Object> data) {
        return "send dingding robot msg";
    }
}
