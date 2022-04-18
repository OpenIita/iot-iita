package cc.iotkit.ruleengine.alert;

import lombok.Data;

import java.util.Map;

/**
 * 邮件告警器
 */
@Data
public class EmailAlerter implements Alerter {


    @Override
    public void setConfig(String config) {

    }

    @Override
    public void setTemplate(String template) {

    }

    @Override
    public String send(Map<String, Object> data) {
        return "send email";
    }
}
