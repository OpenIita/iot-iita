package cc.iotkit.ruleengine.task;

/**
 * 动作执行器接口
 */
public interface ActionExecutor<T> {

    /**
     * 动作名
     */
    String getName();

    /**
     * 执行动作
     */
    void execute(String data);

}
