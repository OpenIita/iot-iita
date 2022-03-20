package cc.iotkit.comp;

public interface Component {

    void create(String config);

    void start();

    void stop();

    void destroy();

    void setHandler(MessageHandler handler);

}
