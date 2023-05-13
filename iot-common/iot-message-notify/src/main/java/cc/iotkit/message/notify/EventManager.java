package cc.iotkit.message.notify;

import cc.iotkit.message.listener.MessageEventListener;
import cc.iotkit.message.model.Message;
import cc.iotkit.message.model.MessageSend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: 石恒
 * date: 2023-05-08 15:17
 * description:
 **/
public class EventManager {

    Map<Enum<EventType>, List<MessageEventListener>> listeners = new HashMap<>();

    public void subscribe(Enum<EventType> eventType, MessageEventListener listener) {
        List<MessageEventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    public void unsubscribe(Enum<EventType> eventType, MessageEventListener listener) {
        List<MessageEventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notify(Enum<EventType> eventType, Message message) {
        List<MessageEventListener> users = listeners.get(eventType);
        for (MessageEventListener listener : users) {
            listener.doEvent(MessageSend.builder().build());
        }
    }

}
