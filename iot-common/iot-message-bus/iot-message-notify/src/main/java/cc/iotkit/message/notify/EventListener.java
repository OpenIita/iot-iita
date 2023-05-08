package cc.iotkit.message.notify;

import cc.iotkit.message.model.Message;

/**
 * author: 石恒
 * date: 2023-05-08 15:08
 * description:
 **/
public interface EventListener {
    void doEvent(Message message);
}
