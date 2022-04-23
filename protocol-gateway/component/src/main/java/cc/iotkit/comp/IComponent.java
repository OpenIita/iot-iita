package cc.iotkit.comp;

public interface IComponent {

    void create(CompConfig config);

    void start();

    void stop();

    void destroy();

    CompConfig getConfig();

    void setScript(String script);

    String getScript();
}
