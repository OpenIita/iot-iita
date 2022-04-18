package cc.iotkit.ruleengine.alert;

import java.util.Map;

/**
 * 报警器
 */
public interface Alerter {

    void setConfig(String config);

    void setTemplate(String template);

    String send(Map<String, Object> data);

}
