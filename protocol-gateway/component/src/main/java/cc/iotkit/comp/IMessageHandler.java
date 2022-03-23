package cc.iotkit.comp;

import java.util.Map;

public interface IMessageHandler {

    void register(Map<String, Object> head, String msg);

    void auth(Map<String, Object> head, String msg);

    void state(Map<String, Object> head, String msg);

    void onReceive(Map<String, Object> head, String type, String msg);
}
