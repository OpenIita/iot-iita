package cc.iotkit.model.rule;

import lombok.Data;

@Data
public class RuleAction {
    /**
     * 动作类型
     */
    protected String type;

    /**
     * 动作配置
     */
    protected String config;

}
