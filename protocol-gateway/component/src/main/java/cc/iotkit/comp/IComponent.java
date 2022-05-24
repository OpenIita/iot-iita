package cc.iotkit.comp;

public interface IComponent {

    String getId();

    void create(CompConfig config);

    void start();

    void stop();

    void destroy();

    CompConfig getConfig();

    void setScript(String script);

    String getScript();

    /**
     * 添加脚本环境变量
     */
    void putScriptEnv(String key, Object value);
}
